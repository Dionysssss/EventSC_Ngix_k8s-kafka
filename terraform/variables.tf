variable "kubeconfig_path" {
  description = "Path to kubeconfig file"
  type        = string
  default     = "~/.kube/config"
}

variable "kubeconfig_context" {
  description = "Kubeconfig context name"
  type        = string
  default     = "docker-desktop"
}

variable "namespace" {
  description = "Kubernetes namespace for the app"
  type        = string
  default     = "eventsc"
}

variable "replicas" {
  description = "Number of replicas for each service"
  type        = number
  default     = 2
}

variable "auth_image" {
  description = "Auth service image"
  type        = string
  default     = "docker.io/lancexie2214/auth-service:latest"
}

variable "event_image" {
  description = "Event service image"
  type        = string
  default     = "docker.io/lancexie2214/event-service:latest"
}

variable "auth_db_url" {
  description = "JDBC URL for auth service DB"
  type        = string
  default     = "jdbc:mysql://host.docker.internal:3307/event_sc?useSSL=false"
}

variable "auth_db_user" {
  description = "Auth DB username"
  type        = string
  default     = "root"
}

variable "auth_db_pass" {
  description = "Auth DB password"
  type        = string
  default     = "devpass"
}

variable "event_db_url" {
  description = "JDBC URL for event service DB"
  type        = string
  default     = "jdbc:mysql://host.docker.internal:3307/event_sc?useSSL=false"
}

variable "event_db_user" {
  description = "Event DB username"
  type        = string
  default     = "root"
}

variable "event_db_pass" {
  description = "Event DB password"
  type        = string
  default     = "devpass"
}

variable "jwt_secret" {
  description = "JWT secret for both services"
  type        = string
  default     = "dev-secret"
}

variable "mail_host" {
  description = "Mail server host"
  type        = string
  default     = "smtp.gmail.com"
}

variable "mail_port" {
  description = "Mail server port"
  type        = number
  default     = 587
}

variable "mail_username" {
  description = "Mail username"
  type        = string
  default     = ""
}

variable "mail_password" {
  description = "Mail password"
  type        = string
  default     = ""
}

variable "ingress_host" {
  description = "Ingress host"
  type        = string
  default     = "eventsc.local"
}
