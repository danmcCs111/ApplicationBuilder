#!/bin/bash
read -p "Enter Youtube API key: " apiKey
apiKeyLoc="Properties/api-keys/youtube-api-key.txt"
echo "API key location: " $apiKeyLoc
echo $apiKey > $apiKeyLoc
