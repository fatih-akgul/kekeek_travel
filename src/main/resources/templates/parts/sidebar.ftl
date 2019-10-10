<script async src='https://cse.google.com/cse.js?cx=partner-pub-${googleAdsensePublisherId}:${googleAdsenseSearchId}'></script><div class="gcse-searchbox-only"></div>
<br>
<h1>Popular</h1>
<ul>
    <#list topVisits as topVisit>
        <li><strong>${topVisit?index + 1}.</strong>&nbsp;<a href="/${topVisit.identifier}" title="${topVisit.counter}">${topVisit.title}</a></li>
    </#list>
</ul>
