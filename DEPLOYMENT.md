# Deployment / Ops Notes

## CI/CD (GitHub Actions)
- Workflow: `.github/workflows/ci.yml` (triggers on branch `YM`).
- Secrets required:
  - `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN` (push `auth-service` and `event-service` images).
- What it does: Maven build (skip tests) → build/push Docker images with tags `latest` and `${GITHUB_SHA}` for both services.
- Extend: add a deploy stage (`kubectl apply` or GitOps) once a remote cluster/kubeconfig is available as a secret.

## Terraform (Kubernetes resources)
- Files: `terraform/main.tf`, `terraform/variables.tf`.
- Default target: kubeconfig context `docker-desktop`, namespace `eventsc`, images `docker.io/lancexie2214/...`.
- Customize via `terraform.tfvars` (DB URLs/creds, JWT secret, mail creds, ingress host, image tags).
- Usage:
  ```bash
  cd terraform
  terraform init
  terraform plan \
    -var="auth_db_pass=devpass" \
    -var="event_db_pass=devpass"
  terraform apply \
    -var="auth_db_pass=devpass" \
    -var="event_db_pass=devpass"
  ```
- Resources managed: Namespace, Secrets/ConfigMap, Deployments, Services, Ingress (auth on `/auth`, event on `/`).

## Local validation (port-forward)
```bash
kubectl -n eventsc port-forward svc/auth-service 8081:8081
kubectl -n eventsc port-forward svc/event-service 8080:8080
# then run the curl scripts used previously for register/login/events
```
