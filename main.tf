provider "azurerm" {
  features {}
}

provider "kubernetes" {
  host                   = azurerm_kubernetes_cluster.aks.kube_config.0.host
  client_certificate     = base64decode(azurerm_kubernetes_cluster.aks.kube_config.0.client_certificate)
  client_key             = base64decode(azurerm_kubernetes_cluster.aks.kube_config.0.client_key)
  cluster_ca_certificate = base64decode(azurerm_kubernetes_cluster.aks.kube_config.0.cluster_ca_certificate)
}

resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_container_registry" "acr" {
  name                = var.acr_name
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  sku                 = "Basic"
  admin_enabled       = true
}

resource "azurerm_kubernetes_cluster" "aks" {
  name                = var.aks_cluster_name
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  dns_prefix          = var.aks_cluster_name

  default_node_pool {
    name       = "default"# Define Azure provider
provider "azurerm" {
  features {}
}
 
# Define resource group
resource "azurerm_resource_group" "revhire_rg" {
  name     = "team4"
  location = "East US"  # Choose a region that supports free tier AKS
}
 
 
# Create Azure Kubernetes Service (AKS) cluster
resource "azurerm_kubernetes_cluster" "revhire_aks" {
  name                = "revhire-aks-cluster"
  location            = azurerm_resource_group.revhire_rg.location
  resource_group_name = azurerm_resource_group.revhire_rg.name
  dns_prefix          = "revhireaks"
 
  default_node_pool {
    name       = "default"
    node_count = 1
    vm_size    = "Standard_B2s"  # Use Standard_B2s for free trial
    vnet_subnet_id = azurerm_subnet.aks_subnet.id
  }
 
  identity {
    type = "SystemAssigned"
  }
 
  network_profile {
    network_plugin = "azure"
    dns_service_ip = "10.2.0.10"
    service_cidr   = "10.2.0.0/16"
  }
}
 
# Create Azure Container Registry (ACR)
resource "azurerm_container_registry" "revhire_acr" {
  name                = "revhireacrsatwik2402"
  resource_group_name = azurerm_resource_group.revhire_rg.name
  location            = azurerm_resource_group.revhire_rg.location
  sku                 = "Basic"
  admin_enabled       = true
}
 
# Assign AcrPull role to AKS
resource "azurerm_role_assignment" "acr_pull" {
  scope                = azurerm_container_registry.revhire_acr.id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_kubernetes_cluster.revhire_aks.kubelet_identity[0].object_id
}
 
 
    node_count = 2
    vm_size    = "Standard_DS2_v2"
  }

  identity {
    type = "SystemAssigned"
  }

  network_profile {
    network_plugin = "azure"
    service_cidr   = "10.0.0.0/16"
    dns_service_ip = "10.0.0.10"
    docker_bridge_cidr = "172.17.0.1/16"
  }

  role_based_access_control {
    enabled = true
  }

  addon_profile {
    oms_agent {
      enabled                    = true
      log_analytics_workspace_id = azurerm_log_analytics_workspace.main.id
    }
  }
}

resource "azurerm_log_analytics_workspace" "main" {
  name                = "${var.resource_group_name}-log-analytics"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  sku                 = "PerGB2018"
  retention_in_days   = 30
}

resource "azurerm_mssql_server" "main" {
  name                         = var.sql_server_name
  resource_group_name          = azurerm_resource_group.main.name
  location                     = azurerm_resource_group.main.location
  version                      = "12.0"
  administrator_login          = var.sql_admin_username
  administrator_login_password = var.sql_admin_password

  identity {
    type = "SystemAssigned"
  }

  tags = {
    environment = "dev"
  }
}

resource "azurerm_mssql_database" "main" {
  name                = var.sql_database_name
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  server_id           = azurerm_mssql_server.main.id
  sku_name            = "S0"
}

output "aks_cluster_name" {
  value = azurerm_kubernetes_cluster.aks.name
}

output "acr_login_server" {
  value = azurerm_container_registry.acr.login_server
}

output "sql_server_name" {
  value = azurerm_mssql_server.main.name
}

output "sql_database_name" {
  value = azurerm_mssql_database.main.name
}
