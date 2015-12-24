SHELL := /bin/bash
PATH := $(PWD)/node_modules/.bin:$(PATH)

test:
	@cd src; mocha

showcase:
	@cd src; ENVIRONMENT=SHOWCASE mocha

staging:
	@cd src; ENVIRONMENT=STAGING mocha

sandbox:
	@echo ${ENVIRONMENT}
	@cd src/sandbox; ENVIRONMENT=${ENVIRONMENT} mocha ${FILE_NAME} --no-timeouts

local:
	@cd src; ENVIRONMENT=LOCAL mocha

datasetup:
	@cd src/utility; ENVIRONMENT=${ENVIRONMENT} node data_setup.js

.PHONY: test showcase staging sandbox local datasetup
