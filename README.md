# VTest

check the basic Internet connectivity stability by checking DNS Server connections and their
respective response times

## Compile to native image:

- Install [GraalVM](https://www.graalvm.org/docs/getting-started/#install-graalvm)
    - [Ubuntu](https://dev.to/fahadisrar/guide-to-install-graalvm-community-edition-on-ubuntu-38h8)
- ```bash
    ./gradlew build
  ```
- ```bash
  /usr/lib/jvm/graalvm/bin/native-image --report-unsupported-elements-at-runtime  -jar build/libs/vtest-1.0-all.jar build/vtest --no-server
  ```
- copy the build binary build/vtest to a location in your path
- run: ```vtest ```