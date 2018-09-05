####################################################################
#
# Rules used to build and release the SDK.
#
####################################################################

SDK_BUILD_IMAGE:=leanplum/android-sdk-build:latest
DOCKER_RUN:=docker run \
			--tty --interactive --rm \
			--volume `pwd`:/leanplum \
			--workdir /leanplum

clean-local-properties:
	rm -f local.properties

sdk: clean-local-properties
	gradle clean assembleDebug testDebugUnitTest --info

sdk-in-container:
	${DOCKER_RUN} ${SDK_BUILD_IMAGE} make sdk

builder-shell:
	${DOCKER_RUN} ${SDK_BUILD_IMAGE} bash

build-image:
	docker build -t ${SDK_BUILD_IMAGE} . -f Tools/jenkins/build.dockerfile
	docker push ${SDK_BUILD_IMAGE}

PYTHON_IMAGE=leanplum/android-tools-python
release: python-image
	${DOCKER_RUN} ${PYTHON_IMAGE} ./Tools/create-release.bash ${TYPE}

python-image:
	docker build -t ${PYTHON_IMAGE} . -f Tools/jenkins/python.dockerfile

python-image-shell: python-image
	${DOCKER_RUN} ${PYTHON_IMAGE} bash

.PHONY: build
