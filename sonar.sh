#!/bin/sh
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=laffer1-github \
    -Dsonar.login=916d745066722e1fe6ecc2213a8aada185da67a2
