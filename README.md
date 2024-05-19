# Trade Store Service

Simple implementation/simulation of the Trade Store. 

Data is stored in memory for now.

Currently, it's a REST based service but the code can be extended to read data from other streaming services or queues in future

Following validations have been implemented

1. During transmission if the lower version is being received by the store it will reject the trade and
   throw an exception. If the version is same it will override the existing record.
2. Store should not allow the trade which has less maturity date then today date.
3. Store should automatically update the expire flag if in a store the trade crosses the maturity
   date.

## Class Diagram
@TBD using intelliJ plugins

## How to build jar locally

```bash
mvn clean install
```

## How to run code locally
```
Run the TradeStoreServiceApplication class in IDE
```

## Sample Request

Add Trade to Store 


```json
API - <localhost:7077>/api/v1/trade
Method = POST
Sample Request Body
{
	"tradeId": "T1",
	"version" : 1,
	"counterPartyId": "C1",
	"bookId": "B1",
	"maturityDate": "12/12/2024",
	"createdDate" : "05/05/2024",
    "expired" : "N"
}
```

Get Trade from Store

@TODO - Follow Rest Standards for API naming/URI conventions/definition 


```json
API - <localhost:7077>/api/v1/trade
Method = GET
Sample Request Body
{
	"tradeId": "T1",
	"version" : 1
}
```



## CI CD integration
Jenkins build file has been provided to build the application code. 
The ideal Jenkins pipeline would have multiple stages but for now we have only got the following stages

1) Code Checkout 
2) Code Build
3) Run Unit Tests

Other stages that might be included as below

4) Unit test code coverage 
5) SAST Analysis (SonarQube, PMD Checkstyle, etc.)
6) Artifactory integration, build tagging etc.
7) If containerized application - Build Docker container 
8) Upload container to container registry

Continuous Delivery Steps
1) Deploy application to ST environment 
2) Run System tests 
3) Deploy application to Integration environment
4) Run Integration Test Cases
5) Run DAST 
6) Run Container Scanning
7) Deploy to UAT environment
8) Run Automation Tests   

Continuous Deployment Steps (if enabled)
1) Deploy to Prod
2) Run PVT test cases
3) Generate release notes

Notifications need to be sent out to appropriate stakeholders at the various critical stages of the pipeline.
Notifications vary per business unit and company so haven't included as part of the pipeline

Steps and stages would vary based on the environment (on-premise, cloud), 
Ideally should have new infrastructure for the deployment per pipeline so IAC steps would be included which may create new environment (especially for cloud based apps)
