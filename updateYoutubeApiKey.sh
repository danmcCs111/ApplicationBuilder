#!/bin/bash
echo "https://developers.google.com/youtube/registering_an_application"
read -p "Enter Youtube API key: " apiKey
apiKeyLoc="Properties/api-keys/youtube-api-key.txt"
echo "API key location: " $apiKeyLoc
echo $apiKey > $apiKeyLoc
