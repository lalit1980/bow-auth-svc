#!/bin/bash
sed "s/tagVersion/$1/g" services.yml >bow_auth_svc.yml