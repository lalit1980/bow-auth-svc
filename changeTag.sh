#!/bin/bash
sed "s/tagVersion/$1/g" services.yml >api_bow.yml