{
"query" : {
"filtered" : {
"filter" : { "terms" : { "g" : "${group}" }},
"query" : { "multi_match": {
	        "query":    "${keywords}",
	        "type":     "best_fields",
	        "fields":   [ "s", "c" ],
	        "tie_breaker":          0.3,
	        "minimum_should_match": "30%"
    	}
    }
}}}


<#-- commented out

{
    "query": {
        "query_string": {
            "query": "${query_string}"
        },
        "term": {
            "${query_field}": "${query_value}"
        }
    }
}



<ul>
    <#list systems as system>
      <li>${system_index + 1}. ${system.name} from ${system.developer}</li>
    </#list>
  </ul>
-->