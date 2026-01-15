job "ollama" {
  datacenters = ["eu-north-1"]
  type = "service"

  group "ollama" {
    count = 1
    constraint {
      attribute = "$${node.class}"
      value     = "gpu"
    }
    network {
      port "api" {
        static = 11434
      }
    }

    task "ollama" {
      driver = "docker"

      env {
        OLLAMA_HOST = "0.0.0.0"
        OLLAMA_DEBUG=1
        NVIDIA_VISIBLE_DEVICES = "all"
        NVIDIA_DRIVER_CAPABILITIES = "compute,utility"
      }

      config {
        image   = "ollama/ollama:latest"
        ports = ["api"]
        runtime = "nvidia"
        volumes = ["local/ollama:/root/.ollama"]
        entrypoint = ["/bin/sh", "-c"]
        args = [
          <<EOH
          ollama serve &
          sleep 10;
          echo "Pulling model llama3...";
          ollama pull bge-m3;
          wait
          EOH
        ]
      }
      resources {
        cpu    = 1000
        memory = 4096
      }

      service {
        name = "ollama"
        port = "api"

        check {
          type     = "http"
          path     = "/"
          interval = "10s"
          timeout  = "2s"
        }
        tags = [
          "traefik.enable=true",
          "traefik.http.routers.ollama.rule=PathPrefix(`/ollama`)",
          "traefik.http.routers.ollama.middlewares=ollama-stripprefix",
          "traefik.http.middlewares.ollama-stripprefix.stripprefix.prefixes=/ollama"
        ]
      }
    }
  }
}
