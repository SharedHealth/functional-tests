SHELL := /bin/bash
PATH := $(PWD)/node_modules/.bin:$(PATH)

test:
	@cd src; mocha

showcase:
	@cd src; ENVIRONMENT=SHOWCASE mocha

.PHONY: test showcase
