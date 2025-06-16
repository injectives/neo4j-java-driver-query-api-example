# Example of using Neo4j Java Driver with Neo4j Query API

Starting with `6.0.0-alpha01` Neo4j Java Driver release, it is possible to connect to Neo4j Query API over HTTP protocol
using an optional and experimental additional module.

Just for clarity, this support is experimental and has a list on limitations. Some of them are listed 
[here](https://github.com/neo4j/bolt-connection-java/blob/main/neo4j-bolt-connection-query-api/README.md#limitataions).
Please make sure you are aware and happy with this before using it.

## Usage

### Opt-in

To use the Query API support, you MUST opt-in by including an extra module to your dependencies alongside the No4j Java
Driver. To ensure compatible versions, please use the Neo4j Java Driver BOM:
```xml
<dependencies>
    <dependency>
        <groupId>org.neo4j.driver</groupId>
        <artifactId>neo4j-java-driver</artifactId>
    </dependency>
    <dependency>
        <groupId>org.neo4j.bolt</groupId>
        <artifactId>neo4j-bolt-connection-query-api</artifactId>
    </dependency>
</dependencies>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.neo4j.driver</groupId>
            <artifactId>neo4j-java-driver-bom</artifactId>
            <type>pom</type>
            <scope>import</scope>
            <version>${neo4j-java-driver.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Driver usage

To use the driver with Query API, create a new driver with `https` or `http` scheme, for example: `http://localhost:7474`.

See [QueryApiIT](./src/test/java/org/injectives/example/query_api/QueryApiIT.java).

## Running example

To run the exmple in this repository, you MUST have Docker and Maven installed. 

To use Neo4j Enterprise Edition, you must accept the license agreement by setting the environment variable 
`NEO4J_ACCEPT_LICENSE_AGREEMENT=yes`.

Sample command:
```bash
NEO4J_ACCEPT_LICENSE_AGREEMENT=yes mvn clean verify
```

