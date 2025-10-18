# Helm Charts Repository

This repository contains Helm charts for the KEA project microservices.

## Charts

- **user-service**: User management microservice
- **sleep-service**: Sleep tracking and analytics microservice

## Usage

### Add this repository to Helm

```bash
helm repo add kea-charts https://github.com/Octa-Cloud/helm-charts
helm repo update
```

### Install a chart

```bash
# Install user-service
helm install user-service kea-charts/user-service

# Install sleep-service
helm install sleep-service kea-charts/sleep-service
```

### Upgrade a chart

```bash
# Upgrade user-service
helm upgrade user-service kea-charts/user-service

# Upgrade sleep-service
helm upgrade sleep-service kea-charts/sleep-service
```

## Development

### Lint charts

```bash
# Lint user-service
helm lint user-service/

# Lint sleep-service
helm lint sleep-service/
```

### Package charts

```bash
# Package user-service
helm package user-service/

# Package sleep-service
helm package sleep-service/
```

### Test charts

```bash
# Test user-service
helm template user-service user-service/ --debug

# Test sleep-service
helm template sleep-service sleep-service/ --debug
```

## Values

Each chart has its own `values.yaml` file with default values. You can override these values by:

1. Creating a custom values file
2. Using `--set` flags
3. Using `--values` flag

Example:

```bash
# Install with custom values
helm install user-service kea-charts/user-service --values custom-values.yaml
helm install sleep-service kea-charts/sleep-service --values custom-values.yaml
```

## Chart Structure

```
user-service/
├── Chart.yaml          # Chart metadata
├── values.yaml         # Default values
├── templates/          # Kubernetes templates
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── ingress.yaml
│   └── ...
└── tests/              # Test templates
    └── test-connection.yaml

sleep-service/
├── Chart.yaml          # Chart metadata
├── values.yaml         # Default values
├── templates/          # Kubernetes templates
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── ingress.yaml
│   └── ...
└── tests/              # Test templates
    └── test-connection.yaml
```

## Service Dependencies

### sleep-service
- **MongoDB**: Sleep data storage (host: 10.0.0.45:27017)
- **Redis**: Caching and session management
- **JWT**: Authentication token validation

### user-service
- **MySQL**: User data storage
- **Redis**: Caching and session management
- **JWT**: Authentication token generation and validation
