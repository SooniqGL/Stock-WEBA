<#-- This is query: exact match multiple groups, and search for keywords;
 and facets are returned as well. -->
{
  "query": {
    "bool": {
      "must":     { "terms": { "g": [${groups}] }},
      "should":  [
        { "match": { "s": "${keywords}" }},
        { "match": { "c": "${keywords}" }}
    	],
      "minimum_should_match": "75%"
    }
  },  
  "facets" : {
      "tags" : { "terms" : {"field" : "g"} }
    }
  
}
