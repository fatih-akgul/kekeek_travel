<ul>
    <li class="nav-0"><a href="/">Home</a></li>
    <#list topNavPages as topNavPage>
        <li class="nav-${topNavPage?index + 1}"><a href="/${topNavPage.identifier}">${topNavPage.title}</a></li>
    </#list>
</ul>
