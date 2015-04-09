SHELL := /bin/bash
PATH := $(PWD)/node_modules/.bin:$(PATH)

test:
	@cd src; mocha

showcase:
	@cd src; ENVIRONMENT=SHOWCASE mocha

staging:
	@cd src; ENVIRONMENT=STAGING mocha


.PHONY: test showcase staging
