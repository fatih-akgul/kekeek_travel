<#if children?has_content && parent?has_content>
    <hr>
</#if>
<div class="breadcrumbs-bottom">
    <#if parent?has_content>
        <a href="${parent.identifier}">${parent.title}</a>
    </#if>
    <#if siblings?has_content>
        &nbsp;<span class="highlight">|</span>&nbsp;
        <#list siblings as sibling>
            <#if sibling.identifier == article.identifier>
                ${sibling.title}
            <#else>
                <a href="/${sibling.identifier}">${sibling.title}</a>
            </#if>
            <#sep>&nbsp;<span class="highlight">|</span>&nbsp;</#sep>
        </#list>
    </#if>
</div>
<#if children?has_content && parent?has_content>
    <hr>
</#if>