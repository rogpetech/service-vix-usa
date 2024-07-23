package com.service.vix.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This class is used to render the upcoming request to related page
 */
@Tag(name = "Dashboard Controller", description = "Handles all dashboard-related requests")
@Controller
@RequestMapping("/dashboard")
public class ApplicationController extends BaseController {

    /**
     * This method is used to handle the request related to super admin dashboard
     * 
     * @author rodolfopeixoto
     * @date May 31, 2023
     * @return String
     * @exception Description
     */
    @Operation(summary = "Redirects to Super Admin Dashboard", description = "Handles the request related to super admin dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirection to super admin dashboard")
    })
    @GetMapping("/super")
    public String superAdminDashboard() {
        return "redirect:/super-admin-dashboard/";
    }

    /**
     * This method is used to handle the request related to user dashboard
     * 
     * @author rodolfopeixoto
     * @date May 31, 2023
     * @return String
     * @exception Description
     */
    @Operation(summary = "Renders User Dashboard", description = "Handles the request related to user dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rendered user dashboard")
    })
    @GetMapping("/user")
    public String userDashboard() {
        return "/user/user-dashboard";
    }

    /**
     * This method is used to handle the request related to admin dashboard
     * 
     * @author rodolfopeixoto
     * @date May 31, 2023
     * @return String
     * @exception Description
     */
    @Operation(summary = "Renders Admin Dashboard", description = "Handles the request related to admin dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rendered admin dashboard")
    })
    @GetMapping("/admin")
    public String adminDashboard() {
        return "/admin/admin-dashboard";
    }

    /**
     * This method is used to handle the request related to mod dashboard
     * 
     * @author rodolfopeixoto
     * @date May 31, 2023
     * @return String
     * @exception Description
     */
    @Operation(summary = "Renders Moderator Dashboard", description = "Handles the request related to mod dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rendered mod dashboard")
    })
    @GetMapping("/mod")
    public String modDashboard() {
        return "/mod/mod-dashboard";
    }

    /**
     * This method is used to handle the request related to organization dashboard
     * 
     * @author rodolfopeixoto
     * @date May 31, 2023
     * @return String
     * @exception Description
     */
    @Operation(summary = "Renders Organization Dashboard", description = "Handles the request related to organization dashboard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rendered organization dashboard")
    })
    @GetMapping("/organization")
    public String organizationDashboard() {
        return "organization/home";
    }
}
