package com.atibusinessgroup.amanyaman.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");

    public static final URI LOCKED_TYPE = URI.create(PROBLEM_BASE_URL + "/locked");
    public static final URI USER_LOCKED_TYPE = URI.create(PROBLEM_BASE_URL + "/user-locked");
    public static final URI BAD_CREDENTIAL_TYPE = URI.create(PROBLEM_BASE_URL + "/bad-credential");
    public static final URI UNLOCKED_TYPE = URI.create(PROBLEM_BASE_URL + "/unlocked");
    public static final URI USER_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/user-not-found");

    private ErrorConstants() {
    }
}
