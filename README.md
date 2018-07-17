Zerobounce Java API wrapper
=====================

[ZeroBounce](https://www.zerobounce.net) Java API


This is a Java wrapper class example for the ZeroBounce API.

The project has 2 dependencies:
1) Apache HttpClient
2) JSON (org.json)

Either download the dependencies separately and add them to
the relevant path or add the following dependencies to your
pom.xml file if you're building via Maven (already added if you're cloning this repo):

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.4</version>
</dependency>

<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20171018</version>
</dependency>
```

**Class, Properties, and Methods:**

<b><i>ZeroBounceApi</b></i> Class:

|<b>Parameters</b>|<b>Descriptions</b> 
|:--- |:--- 
apiKey  | Located in your account.. 
timeoutSeconds | Timeout settings in seconds, setting this enables you to control how long you are willing to wait for a response from the API. When the timeout occurs an "Unknown" result is returned.

<b><i>Validate(email)</b></i> method:
  
|<b>Property</b>|<b>Possible Values</b> 
|:--- |:--- 
getEmailAddress()  | The email address you are validating. 
getStatus() | Valid /Invalid /Catch-All /Unknown /Spamtrap /Abuse /DoNotMail 
getSubStatus()  |antispam_system /greylisted /mail_server_temporary_error /forcible_disconnect /mail_server_did_not_respond /timeout_exceeded /failed_smtp_connection /mailbox_quota_exceeded /exception_occurred /possible_traps /role_based /global_suppression /mailbox_not_found /no_dns_entries /failed_syntax_check /possible_typo /unroutable_ip_address /leading_period_removed /does_not_accept_mail /alias_address
getAccount() | The portion of the email address before the "@" symbol.
getDomain() | The portion of the email address after the "@" symbol
isDisposable() |[true/false] If the email domain is diposable, which are usually temporary email addresses.
isToxic() |[true/false] These domains are known for abuse, spam, and bot created.
getFirstName()  | The first name of the owner of the email when available or [null].
getLastName()  |The last name of the owner of the email when available or [null].
getGender() |The gender of the owner of the email when available or [null].
getCreationDate() |The creation date of the email when available or [null].
getLocation() |The location of the owner of the email when available or [null].
getProcessedAt() |The UTC time the email was validated.

<b><i>validateWithIpAddress(email, ip)(email)</b></i> method:
These additional properties are available with the ValidateWithIpAddress method.

|<b>Property</b>|<b>Possible Values</b> 
|:--- |:--- 
getCountry()  | The country the IP address is from. 
getCity() | The city the IP address is from.
getZipcode() | The zip code the IP address is from.
getRegion() | The region/state the IP address is from.

<b><i>GetCredit</b></i> method:

|<b>Property</b>|<b>Possible Values</b> 
|:--- |:--- 
getCredits()  | The number of credits left in account for email validation.

**Any of the following email addresses can be used for testing the API, no credits are charged for these test email addresses:**
+ disposable@example.com
+ invalid@example.com
+ valid@example.com
+ toxic@example.com
+ donotmail@example.com
+ spamtrap@example.com
+ abuse@example.com
+ unknown@example.com
+ catch_all@example.com
+ antispam_system@example.com
+ does_not_accept_mail@example.com
+ exception_occurred@example.com
+ failed_smtp_connection@example.com
+ failed_syntax_check@example.com
+ forcible_disconnect@example.com
+ global_suppression@example.com
+ greylisted@example.com
+ leading_period_removed@example.com
+ mail_server_did_not_respond@example.com
+ mail_server_temporary_error@example.com
+ mailbox_quota_exceeded@example.com
+ mailbox_not_found@example.com
+ no_dns_entries@example.com
+ possible_trap@example.com
+ possible_typo@example.com
+ role_based@example.com
+ timeout_exceeded@example.com
+ unroutable_ip_address@example.com

**You can this IP to test the GEO Location in the API.**

+ 99.110.204.1

##### Code Sample:

The validation methods return objects on which you call get methods which return the relevant information. Please see the code for all getters and below for a sample:

```java
//Depending on how you use the API, you might want it to time out faster,
//for example on a registration screen. 
//Normally the API will return results very fast, but a small percentage of
//mail servers take upwards of 20+ seconds to respond. 
//If the API times out, it will return a status of "Unknown" and 
//a sub_status of "timeout_exceeded" 

ZeroBounceApi zeroBounceApi = new ZeroBounceApi("YOUR_API_KEY",TIMEOUT_IN_SECONDS);

// Get credits and assign to int variable
int credits = zeroBounceApi.getCredits();

// validate email and assign result to a ZeroBounceResponse object
ZeroBounceResponse validation = zeroBounceApi.validate("some@email.com");
validation.getEmailAddress();
validation.getStatus();
validation.isDisposable();

// validate email with ip and assign result to a ZeroBounceResponseWithIp object
ZeroBounceResponseWithIp validationWithIp = zeroBounceApi.validateWithIpAddress("some@email.com", "some.ip.address");
validationWithIp.getEmailAddress();
validationWithIp.getCountry();
```
