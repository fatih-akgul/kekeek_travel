<a href="/">Home</a> <span class="highlight">&nbsp;&gt;&gt;&nbsp;</span>
<#list breadcrumbs as breadcrumb>
    <#if breadcrumb.identifier == article.identifier>
        ${breadcrumb.title}
    <#else>
        <a href="/${breadcrumb.identifier}">${breadcrumb.title}</a>
    </#if>

    <#sep><span class="highlight">&nbsp;&gt;&gt;&nbsp;</span></#sep>
</#list>