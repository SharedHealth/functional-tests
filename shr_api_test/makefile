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

.PHONY: test showcase staging sandbox
