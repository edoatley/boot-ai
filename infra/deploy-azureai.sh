#!/bin/bash
# Create Azure AI Environment

echo "1. Creating Resource Group $RESOURCE_GROUP"
az group create \
    --name $RESOURCE_GROUP \
    --location $LOCATION

# Deploy cognitive services
echo "2. Creating cognitive services $COGNITIVE_SVCS_NAME"
az cognitiveservices account create \
    --name $COGNITIVE_SVCS_NAME \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --kind OpenAI \
    --sku s0 \
    --subscription $SUBSCRIPTION_ID

# Wait for the cognitive services account to be created
echo "Waiting for the Cognitive Services account to be created..."
sleep 60
az resource wait --exists \
  --name $COGNITIVE_SVCS_NAME \
  --resource-group $RESOURCE_GROUP \
  --namespace "Microsoft.CognitiveServices" \
  --resource-type "accounts"

# Collect relevant details
echo "3. Setting ENDPOINT_URLS"
ENDPOINT_URLS=$(az cognitiveservices account show \
    --name $COGNITIVE_SVCS_NAME \
    --resource-group $RESOURCE_GROUP \
    --query properties.endpoints \
    --output yamlc)

echo "4. Setting PRIMARY_API_KEY"
PRIMARY_API_KEY=$(az cognitiveservices account keys list \
    --name $COGNITIVE_SVCS_NAME \
    --resource-group $RESOURCE_GROUP \
    --query key1 \
    --output tsv)

echo "5. Setting AVAILABLE_MODELS"
AVAILABLE_MODELS=$(az cognitiveservices model list \
    --location $LOCATION \
    --query '[].{ModelName:model.name,ModelVersion:model.version,ModelFormat:model.format}' \
    --output table)

# Create an account with a specific model
echo "6.Creating $COG_SVCS_EMBED_MODEL_NAME-deployment"
az cognitiveservices account deployment create \
    --name $COGNITIVE_SVCS_NAME \
    --resource-group  $RESOURCE_GROUP \
    --deployment-name "$COG_SVCS_EMBED_MODEL_NAME-deployment" \
    --model-name $COG_SVCS_EMBED_MODEL_NAME \
    --model-version "$COG_SVCS_EMBED_MODEL_VERSION"  \
    --model-format $COG_SVCS_EMBED_MODEL_FORMAT \
    --sku-capacity "1"

echo "7.Creating $COG_SVCS_GEN_AI_MODEL_NAME-deployment"
az cognitiveservices account deployment create \
    --name $COGNITIVE_SVCS_NAME \
    --resource-group  $RESOURCE_GROUP \
    --deployment-name "$COG_SVCS_GEN_AI_MODEL_NAME-deployment" \
    --model-name $COG_SVCS_GEN_AI_MODEL_NAME \
    --model-version "$COG_SVCS_GEN_AI_MODEL_VERSION"  \
    --model-format $COG_SVCS_GEN_AI_MODEL_FORMAT \
    --sku-capacity "1" \
    --sku-name "GlobalStandard"