job "hf-embeddings" {
  datacenters = ["eu-north-1"]
  type        = "service"

  group "tei" {
    count = 1

    constraint {
      attribute = "$${node.class}"
      value     = "gpu"
    }

    network {
      port "http" {
        static = 8080
      }
    }

    task "tei" {
      driver = "docker"

      env {
        NVIDIA_VISIBLE_DEVICES        = "all"
        NVIDIA_DRIVER_CAPABILITIES    = "compute,utility"
        HF_HUB_ENABLE_HF_TRANSFER     = "1"
      }

      config {
        image   = "ghcr.io/huggingface/text-embeddings-inference:latest"
        ports   = ["http"]
        runtime = "nvidia"
        volumes = [
          "local/tei:/data"
        ]
        args = [
          "--model-id", "BAAI/bge-m3",
          "--hostname", "0.0.0.0",
          "--port", "8080",
          "--max-client-batch-size", "128",
          "--max-batch-tokens", "16384",
        ]
      }

      resources {
        cpu    = 2000
        memory = 4096
      }

      service {
        name = "hf-embeddings"
        port = "http"

        check {
          type     = "http"
          path     = "/health"
          interval = "10s"
          timeout  = "2s"
        }

        tags = [
          "traefik.enable=true",
          "traefik.http.routers.hf-embeddings.rule=PathPrefix(`/embeddings`)",
          "traefik.http.routers.hf-embeddings.middlewares=hf-embeddings-stripprefix",
          "traefik.http.middlewares.hf-embeddings-stripprefix.stripprefix.prefixes=/embeddings"
        ]
      }
    }
  }
}
