package com.hlopg.utils

import java.util.regex.Pattern

object ValidationUtils {

    // Email validation pattern
    private val EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9+._%-+]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                ")+"
    )

    // Indian phone number pattern (10 digits)
    private val PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$")

    /**
     * Validates email format
     * @param email The email address to validate
     * @return true if email is valid, false otherwise
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && EMAIL_PATTERN.matcher(email).matches()
    }

    /**
     * Validates phone number format (Indian format)
     * @param phone The phone number to validate
     * @return true if phone number is valid, false otherwise
     */
    fun isValidPhoneNumber(phone: String): Boolean {
        val cleanedPhone = phone.replace("\\s".toRegex(), "").replace("+91", "")
        return PHONE_PATTERN.matcher(cleanedPhone).matches()
    }

    /**
     * Checks if input is email or phone number
     * @param input The input string to check
     * @return "email" if valid email, "phone" if valid phone, "invalid" otherwise
     */
    fun identifyInputType(input: String): InputType {
        return when {
            isValidEmail(input) -> InputType.EMAIL
            isValidPhoneNumber(input) -> InputType.PHONE
            else -> InputType.INVALID
        }
    }

    /**
     * Validates password strength
     * @param password The password to validate
     * @return ValidationResult with isValid flag and error message
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password cannot be empty")
            password.length < 8 -> ValidationResult(false, "Password must be at least 8 characters")
            !password.any { it.isUpperCase() } -> ValidationResult(false, "Password must contain at least one uppercase letter")
            !password.any { it.isLowerCase() } -> ValidationResult(false, "Password must contain at least one lowercase letter")
            !password.any { it.isDigit() } -> ValidationResult(false, "Password must contain at least one digit")
            else -> ValidationResult(true, "")
        }
    }

    /**
     * Validates if passwords match
     * @param password The password
     * @param confirmPassword The confirmation password
     * @return true if passwords match, false otherwise
     */
    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password.isNotEmpty() && password == confirmPassword
    }

    /**
     * Validates full name
     * @param name The name to validate
     * @return ValidationResult with isValid flag and error message
     */
    fun validateFullName(name: String): ValidationResult {
        return when {
            name.isEmpty() -> ValidationResult(false, "Name cannot be empty")
            name.length < 2 -> ValidationResult(false, "Name must be at least 2 characters")
            !name.matches(Regex("^[a-zA-Z\\s]+$")) -> ValidationResult(false, "Name can only contain letters and spaces")
            else -> ValidationResult(true, "")
        }
    }

    /**
     * Formats phone number to include +91 prefix
     * @param phone The phone number to format
     * @return Formatted phone number with +91 prefix
     */
    fun formatPhoneNumber(phone: String): String {
        val cleanedPhone = phone.replace("\\s".toRegex(), "").replace("+91", "")
        return if (isValidPhoneNumber(cleanedPhone)) {
            "+91$cleanedPhone"
        } else {
            phone
        }
    }

    /**
     * Validates all sign up fields
     * @return ValidationResult with isValid flag and error message
     */
    fun validateSignUpForm(
        fullName: String,
        mobileNumber: String,
        email: String,
        gender: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        // Validate full name
        val nameValidation = validateFullName(fullName)
        if (!nameValidation.isValid) return nameValidation

        // Validate mobile number
        if (!isValidPhoneNumber(mobileNumber)) {
            return ValidationResult(false, "Please enter a valid 10-digit mobile number")
        }

        // Validate email
        if (!isValidEmail(email)) {
            return ValidationResult(false, "Please enter a valid email address")
        }

        // Validate gender
        if (gender.isEmpty()) {
            return ValidationResult(false, "Please select your gender")
        }

        // Validate password
        val passwordValidation = validatePassword(password)
        if (!passwordValidation.isValid) return passwordValidation

        // Validate password match
        if (!doPasswordsMatch(password, confirmPassword)) {
            return ValidationResult(false, "Passwords do not match")
        }

        return ValidationResult(true, "")
    }

    /**
     * Validates login form
     * @return ValidationResult with isValid flag and error message
     */
    fun validateLoginForm(
        emailOrPhone: String,
        password: String
    ): ValidationResult {
        // Validate email or phone
        val inputType = identifyInputType(emailOrPhone)
        if (inputType == InputType.INVALID) {
            return ValidationResult(false, "Please enter a valid email address or mobile number")
        }

        // Validate password
        if (password.isEmpty()) {
            return ValidationResult(false, "Password cannot be empty")
        }

        if (password.length < 8) {
            return ValidationResult(false, "Password must be at least 8 characters")
        }

        return ValidationResult(true, "")
    }
}

/**
 * Enum for input type identification
 */
enum class InputType {
    EMAIL,
    PHONE,
    INVALID
}

/**
 * Data class for validation results
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String
)