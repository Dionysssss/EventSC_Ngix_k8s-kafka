terraform {
  required_version = ">= 1.3.0"
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.30"
    }
  }
}

provider "kubernetes" {
  config_path    = var.kubeconfig_path
  config_context = var.kubeconfig_context
}

resource "kubernetes_namespace" "eventsc" {
  metadata {
    name = var.namespace
  }
}

resource "kubernetes_secret" "auth_db" {
  metadata {
    name      = "auth-db"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
  }
  type = "Opaque"
  string_data = {
    url      = var.auth_db_url
    username = var.auth_db_user
    password = var.auth_db_pass
  }
}

resource "kubernetes_secret" "event_db" {
  metadata {
    name      = "event-db"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
  }
  type = "Opaque"
  string_data = {
    url      = var.event_db_url
    username = var.event_db_user
    password = var.event_db_pass
  }
}

resource "kubernetes_secret" "jwt_secret" {
  metadata {
    name      = "jwt-secret"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
  }
  type = "Opaque"
  string_data = {
    secret = var.jwt_secret
  }
}

resource "kubernetes_config_map" "mail_config" {
  metadata {
    name      = "mail-config"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
  }
  data = {
    host = var.mail_host
    port = tostring(var.mail_port)
  }
}

resource "kubernetes_secret" "mail_secret" {
  metadata {
    name      = "mail-secret"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
  }
  type = "Opaque"
  string_data = {
    username = var.mail_username
    password = var.mail_password
  }
}

resource "kubernetes_deployment" "auth_service" {
  metadata {
    name      = "auth-service"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
    labels = {
      app = "auth-service"
    }
  }
  spec {
    replicas = var.replicas
    selector {
      match_labels = {
        app = "auth-service"
      }
    }
    template {
      metadata {
        labels = {
          app = "auth-service"
        }
      }
      spec {
        container {
          name  = "auth-service"
          image = var.auth_image
          port {
            container_port = 8081
          }
          env {
            name = "DB_URL"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.auth_db.metadata[0].name
                key  = "url"
              }
            }
          }
          env {
            name = "DB_USER"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.auth_db.metadata[0].name
                key  = "username"
              }
            }
          }
          env {
            name = "DB_PASS"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.auth_db.metadata[0].name
                key  = "password"
              }
            }
          }
          env {
            name = "JWT_SECRET"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.jwt_secret.metadata[0].name
                key  = "secret"
              }
            }
          }
          env {
            name = "MAIL_HOST"
            value_from {
              config_map_key_ref {
                name = kubernetes_config_map.mail_config.metadata[0].name
                key  = "host"
              }
            }
          }
          env {
            name = "MAIL_PORT"
            value_from {
              config_map_key_ref {
                name = kubernetes_config_map.mail_config.metadata[0].name
                key  = "port"
              }
            }
          }
          env {
            name = "MAIL_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.mail_secret.metadata[0].name
                key  = "username"
              }
            }
          }
          env {
            name = "MAIL_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.mail_secret.metadata[0].name
                key  = "password"
              }
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "auth_service" {
  metadata {
    name      = "auth-service"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
    labels = {
      app = "auth-service"
    }
  }
  spec {
    selector = {
      app = "auth-service"
    }
    port {
      port        = 8081
      target_port = 8081
    }
  }
}

resource "kubernetes_deployment" "event_service" {
  metadata {
    name      = "event-service"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
    labels = {
      app = "event-service"
    }
  }
  spec {
    replicas = var.replicas
    selector {
      match_labels = {
        app = "event-service"
      }
    }
    template {
      metadata {
        labels = {
          app = "event-service"
        }
      }
      spec {
        container {
          name  = "event-service"
          image = var.event_image
          port {
            container_port = 8080
          }
          env {
            name = "DB_URL"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.event_db.metadata[0].name
                key  = "url"
              }
            }
          }
          env {
            name = "DB_USER"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.event_db.metadata[0].name
                key  = "username"
              }
            }
          }
          env {
            name = "DB_PASS"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.event_db.metadata[0].name
                key  = "password"
              }
            }
          }
          env {
            name = "JWT_SECRET"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.jwt_secret.metadata[0].name
                key  = "secret"
              }
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "event_service" {
  metadata {
    name      = "event-service"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
    labels = {
      app = "event-service"
    }
  }
  spec {
    selector = {
      app = "event-service"
    }
    port {
      port        = 8080
      target_port = 8080
    }
  }
}

resource "kubernetes_ingress_v1" "eventsc" {
  metadata {
    name      = "eventsc-ingress"
    namespace = kubernetes_namespace.eventsc.metadata[0].name
  }
  spec {
    rule {
      host = var.ingress_host
      http {
        path {
          path = "/auth"
          path_type = "Prefix"
          backend {
            service {
              name = kubernetes_service.auth_service.metadata[0].name
              port {
                number = 8081
              }
            }
          }
        }
        path {
          path = "/"
          path_type = "Prefix"
          backend {
            service {
              name = kubernetes_service.event_service.metadata[0].name
              port {
                number = 8080
              }
            }
          }
        }
      }
    }
  }
}
