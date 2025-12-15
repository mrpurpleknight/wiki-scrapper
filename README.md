
> ## Changes
> **api module**
> - Added `WikiExceptionHandler` with `@RestControllerAdvice` for centralising API error responses
> - Added `WikiLinkValidator` for validating incoming links and preventing Path Traversal attacks
> - Added `ValidatingWikiReaderClient` decorator to add `WikiLinkValidator` functionality before the link is processed in the domain module
> - Added appropriate response body and error codes for both success and error scenarios in `WikiScrapperResource`. (404 for not found pages, 400 for malformed requests or invalid links and 500 for invalid page content or missing selfLink)
> - Added unit tests for `ValidatingWikiReaderClient` and `WikiLinkValidator`
>
> **app module**
> - Wired required dependencies in `WikiScrapperConfiguration`
> - Added integration tests for `WikiScrapperApplication` covering both success and failure scenarios, including tests for the `json` profile (and also `html` profile)
>
> **domain module**
> - Added `WikiReaderClient` interface for handling different reading strategies
> - Added domain related exceptions (`WikiInvalidPage`, `WikiPageNotFound`)
> - Added `WikiPageRepository` interface for avoiding direct dependency on persistence module
> - Implemented simple Breadth-First Search (BFS) algorithm in `WikiScrapper` to efficiently traverse and scrape wiki pages while avoiding cycles
> - Added unit tests for all domain module classes
>
> **htmlwikiclient module**
> - Implemented `HtmlWikiReaderClient` using Jsoup for HTML parsing
>
> **jsonwikiclient module**
> - Implemented `JsonWikiReaderClient` using Jackson for JSON parsing
>
> **persistence module**
> - Added a log statement in `WikiPageRepositotyImpl` to log when a page should be saved to the database


# WIKI SCRAPPER

There is a hypothesis that from any wikipedia page, you can access any other page via included links at the bottom of the site.
For this task, we'll assume that this hypothesis is true. Your task is to implement a mechanism that will scrap
whole wikipedia from any root page and save it to the DB.

* You don't have to worry about DB part, it's already implemented.

## Acceptance Criteria
1. After using POST `/wiki/scrap` with root link that does exist, program should scrap all the pages in wikipedia and save them to the database
   1. Body of the POST: `"http://wikiscrapper.test/site1"` (with `"`)
   2. When `html` profile is being set it should scrap wiki using html client 
   3. When `json` profile is being set it should scrap wiki using json client
2. After using POST `/wiki/scrap` with root link that doesn't exist, program should return 404

## Implementation steps

1. Implement `WikiScrapper` class in domain module considering the following:
   1. Write tests for domain module
   2. Think about looped wikiPages (for example, wikiPage1 -> wikiPage2 & wikiPage2 -> wikiPage1)
   3. It should be able to work with both `html` & `json` readers. This means that you need to implement both `HtmlClient` and `JsonClient` so that they can be used interchangeably by `WikiScrapper` (choose whatever strategy you consider best to achieve this). Check local resources in the modules in order to understand the format used.
2. Wire deps in `WikiScrapperConfiguration` 
3. Implement `WikiScrapperResource` to call `WikiScrapper`
   1. 404 httpStatus should be returned for not existing `WikiPage`
   2. Implement some automated tests for rest requests (check 404 & 200 case)
   3. Implement some automated tests which uses `json` profile and read some `json` wikipage

## Hints

1. It's allowed to use any testing framework (for example, Mockito for mocking)
2. It's allowed and recommended using external libraries to read both `html` & `json` files
3. Domain module shouldn't have dependencies to any other module


## Submitting task

Task implementation should be shared on some public repository (for example github)

