<#-- This is query: exact match one group, and search for keywords -->
{
  "query": {
    "bool": {
      "must":     { "terms": { "g": "${group}" }},
      "should":  [
        { "match": { "s": "${keywords}" }},
        { "match": { "c": "${keywords}" }}
    	],
      "minimum_should_match": "75%"
    }
  }
}


