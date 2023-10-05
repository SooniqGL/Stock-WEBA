<#-- This is query: match prefix, and for typing suggestion
Note: max 50 is searched; slop is to allow some word order flexibility.
And multiple groups are used in the search.
 -->
{
  "query": {
    "bool": {
      "must":     { "terms": { "g": [${groups}] }},
      "should": [ 
		    { "match_phrase_prefix" : {
		        "s" : {
		            "query": "${keywords}",
		            "slop":  10,
		            "max_expansions": 50
		        }
		    }},
		    { "match_phrase_prefix" : {
		        "c" : {
		            "query": "${keywords}",
		            "slop":  10,
		            "max_expansions": 50
		        }
		    }}
		],
		"minimum_should_match": "75%"
    }
  }
}



