package com.flexe.outbound.write.controller

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@WebFilter("*")
class FlexeHeaderFilter: Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val correlationId = (request as HttpServletRequest).getHeader(HEADER_NAME)
        (response as HttpServletResponse).setHeader(HEADER_NAME, correlationId)
        chain?.doFilter(request, response)
    }

    companion object {
        val HEADER_NAME = "X-FLEXE-correlationId"
    }
}