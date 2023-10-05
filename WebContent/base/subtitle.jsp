<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%-- We use the subtitleText passed from Struts tiles file.  However, as the cascade is not working for this version of tiles/struts,
   we use a trick to put subtitle.jsp in the same level on the parent layout using jsp:include. --%>
<span style="FONT-FAMILY: Times New Roman, sans-serif, Verdana; COLOR:#006699; FONT-SIZE:28pt; FONT-WEIGHT:bold; display:table; margin:auto;">
<tiles:insertAttribute name="subtitleText" ignore="true" />
</span>
      	