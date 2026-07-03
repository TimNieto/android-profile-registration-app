package com.timnieto.profileregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    static final String EXTRA_GIVEN_NAME = "extra_given_name";
    static final String EXTRA_SURNAME = "extra_surname";
    static final String EXTRA_BIRTHDAY = "extra_birthday";
    static final String EXTRA_GENDER = "extra_gender";
    static final String EXTRA_MOBILE = "extra_mobile";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final int MOBILE_NUMBER_LENGTH = 11;
    private static final int MINIMUM_BIRTH_YEAR = 1900;

    private static final Set<String> VALID_GENDERS = Set.of(
            "male",
            "female",
            "other",
            "prefer not to say"
    );

    private static final Set<String> VALID_MOBILE_PREFIXES = Set.of(
            "0811", "0813", "0817",
            "0895", "0896", "0897", "0898",
            "0905", "0906", "0907", "0908", "0909",
            "0910", "0912", "0913", "0914", "0915", "0916", "0917", "0918", "0919",
            "0920", "0921", "0922", "0923", "0924", "0925", "0926", "0927", "0928", "0929",
            "0930", "0931", "0932", "0933", "0934", "0935", "0936", "0937", "0938", "0939",
            "0940", "0941", "0942", "0943", "0944", "0945", "0946", "0947", "0948", "0949",
            "0950", "0951", "0955", "0956",
            "0961", "0963", "0965", "0966", "0967", "0968", "0969",
            "0970", "0975", "0976", "0977",
            "0981", "0989",
            "0991", "0992", "0993", "0994", "0995", "0997", "0998", "0999"
    );

    private EditText givenNameEditText;
    private EditText surnameEditText;
    private EditText birthdayEditText;
    private EditText genderEditText;
    private EditText mobileEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        givenNameEditText = findViewById(R.id.etGiven);
        surnameEditText = findViewById(R.id.etSurname);
        birthdayEditText = findViewById(R.id.etBirthday);
        genderEditText = findViewById(R.id.etGender);
        mobileEditText = findViewById(R.id.etMobile);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
        findViewById(R.id.btnRegister).setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String givenName = cleanName(getInput(givenNameEditText));
        String surname = cleanName(getInput(surnameEditText));
        String birthday = getInput(birthdayEditText);
        String gender = cleanGender(getInput(genderEditText));
        String mobile = normalizeMobileNumber(getInput(mobileEditText));

        if (!validateName(givenNameEditText, givenName, "Given name")) {
            return;
        }

        if (!validateName(surnameEditText, surname, "Surname")) {
            return;
        }

        if (!validateBirthday(birthday)) {
            return;
        }

        if (!validateGender(gender)) {
            return;
        }

        if (!validateMobileNumber(mobile)) {
            return;
        }

        openResultScreen(givenName, surname, birthday, formatGender(gender), mobile);
    }

    private String getInput(EditText editText) {
        return editText.getText().toString().trim();
    }

    private String cleanName(String value) {
        return value.replaceAll("\\s+", " ");
    }

    private boolean validateName(EditText editText, String value, String fieldName) {
        if (value.isEmpty()) {
            showFieldError(editText, fieldName + " is required");
            return false;
        }

        if (value.length() < 2) {
            showFieldError(editText, fieldName + " must be at least 2 characters");
            return false;
        }

        if (value.length() > 40) {
            showFieldError(editText, fieldName + " must be 40 characters or less");
            return false;
        }

        if (!value.matches("^[A-Za-z]+(?:[ '-][A-Za-z]+)*$")) {
            showFieldError(editText, fieldName + " can only contain letters, spaces, hyphens, or apostrophes");
            return false;
        }

        editText.setError(null);
        return true;
    }

    private boolean validateBirthday(String birthday) {
        if (birthday.isEmpty()) {
            showFieldError(birthdayEditText, "Birthday is required");
            return false;
        }

        if (!birthday.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            showFieldError(birthdayEditText, "Use exact format yyyy-mm-dd");
            return false;
        }

        Date birthDate = parseBirthday(birthday);

        if (birthDate == null) {
            showFieldError(birthdayEditText, "Enter a valid calendar date");
            return false;
        }

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        int birthYear = birthCalendar.get(Calendar.YEAR);

        if (birthYear < MINIMUM_BIRTH_YEAR) {
            showFieldError(birthdayEditText, "Birth year must be 1900 or later");
            return false;
        }

        if (birthDate.after(new Date())) {
            showFieldError(birthdayEditText, "Birthday cannot be in the future");
            return false;
        }

        birthdayEditText.setError(null);
        return true;
    }

    private Date parseBirthday(String birthday) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(birthday);
        } catch (ParseException exception) {
            return null;
        }
    }

    private String cleanGender(String gender) {
        return gender.trim().replaceAll("\\s+", " ").toLowerCase(Locale.US);
    }

    private boolean validateGender(String gender) {
        if (gender.isEmpty()) {
            showFieldError(genderEditText, "Gender is required");
            return false;
        }

        if (!VALID_GENDERS.contains(gender)) {
            showFieldError(genderEditText, "Use Male, Female, Other, or Prefer not to say");
            return false;
        }

        genderEditText.setError(null);
        return true;
    }

    private String formatGender(String gender) {
        if (gender.equals("male")) {
            return "Male";
        }

        if (gender.equals("female")) {
            return "Female";
        }

        if (gender.equals("other")) {
            return "Other";
        }

        return "Prefer not to say";
    }

    private String normalizeMobileNumber(String mobile) {
        return mobile.replaceAll("\\D", "");
    }

    private boolean validateMobileNumber(String mobile) {
        if (mobile.isEmpty()) {
            showFieldError(mobileEditText, "Mobile number is required");
            return false;
        }

        if (mobile.length() != MOBILE_NUMBER_LENGTH) {
            showFieldError(mobileEditText, "Mobile number must be exactly 11 digits");
            return false;
        }

        if (!(mobile.startsWith("09") || mobile.startsWith("08"))) {
            showFieldError(mobileEditText, "Mobile number must start with 09 or 08");
            return false;
        }

        String prefix = mobile.substring(0, 4);

        if (!VALID_MOBILE_PREFIXES.contains(prefix)) {
            showFieldError(mobileEditText, "Unknown PH mobile network prefix");
            return false;
        }

        mobileEditText.setError(null);
        return true;
    }

    private void showFieldError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openResultScreen(String givenName, String surname, String birthday, String gender, String mobile) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_GIVEN_NAME, givenName);
        intent.putExtra(EXTRA_SURNAME, surname);
        intent.putExtra(EXTRA_BIRTHDAY, birthday);
        intent.putExtra(EXTRA_GENDER, gender);
        intent.putExtra(EXTRA_MOBILE, mobile);

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}