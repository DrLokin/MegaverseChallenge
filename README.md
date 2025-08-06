# POLYanetChallenge

### Introduction
The code I present here is a working solution to Phase 1 and 2 of the Crossmint megaverse challenge.
The code is entirely written in Java, and I designed it in such a way that it tries to balance out both
simplicity and complexity, and leaving room for growth should it be necessary to improve upon the code
based on future demands.


### Planning
The first thing I noticed from the challenge is that it was heavily recommended to use the API call to the
candidate's target megaverse. I used Postman to trigger the GET request for the 
Megaverse goal map, and review the html that contained the target megaverse. The response body of the goal
request consisted of json string from which a 2D string array could be extracted, and what would need to be implemented 
in the code would be the logic to read that array and use the api POST requests to 
generate the desired Megaverse.

The steps that would need to be taken in the code to achive the desired result could be summarized as follows:

1. Call the goal map request to get the goal map JSON string.
2. Convert that json into a 2D String array.
3. Convert each element in the array to an object that represents the astral object.
4. Trigger the necessary request for each object.

I decided to create an abstract class that would define the basic fields that each astral object should have,
and have the child classes define their own specific fields. In addition, I decided
to add a factory method in abstract class that would generate an astral object with
as an instance of the correct child class depending on the string from the 2D goal array and its index.
I did this as a means to maximize encapsulation in the code. As for the bodies of the POST api requests,
I decided against using the model classes as the classes that need to be mapped to convert the astral
object into the json body of the request. I considered the possibility that it would be more appropriate to 
have astral object model classes and request body classes be independent of each other. In common practice,
classes that are used to handle data within the could carry information that is irrelevant 
or too sensitive to include in the json of an http request body; it is best to map to another POJO
the necessary information. In this case, astral objects do not need to contain the candidate ID; only the
POJOs for the request bodies should have that information attached.

In regards to calling the API, I decided to create a class that would handle 
calling the requests, as well as the potential issues or errors that may arise.
One of the things that I noticed is that the root link given to us in the challenge
results in a 308 redirect code if called directly. I decided to add logic that would handle
the redirect. Furthermore, I also encountered the issue of the 429 status due to too many requests
called. To fix this issue, I added an error catch that would retry the request after waiting for some time. In addition,
I added a small amount of delay between each request call in the main method to reduce the number of times 
the code has to wait until retrying again.

All the steps mentioned prior to achieve the target megaverse are put together in the main method.
After creating a list of astral objects, the code filters the null elements, representing space,
and then calls the post request for each specific astral object. Each time it will print the status code 
for the request made for a specific astral object. There will be a few times it will wait due
too many requests, but the code achieves it target without any user input.


## Potential Improvements
There are A LOT of improvements that could be done to make this code far more resilient,
but that would have to wait for later, since, as of now, I do not have much time available.

In regards to mapping between objects such as the astral object and the
json model classes, I could have leveraged this using mapstruct to define 
and autogenerate the mapping methods to map each class to another. It would be
the ideal solution in the case that more models are added to the application
and extensive mapping is required. For now, simple mapping is enough for the context of the 
challenge.

The use of the spring framework would have definitely given more possibilities and 
leeway to make the code far more robust. For example, I could have used spring retry to
define methods that would handle retries in case of an error, as well the number of times
to retry and the delay between each retry. I could have used spring autoconfigure or spring
integration for autoconfiguring the api service class along with its properties, and automate
the consumption of each astral object to trigger an http call. In fact, I could have added some
asynchronous processing (multithreading) to call multiple requests at the same time in a 
spring managed thread pool.