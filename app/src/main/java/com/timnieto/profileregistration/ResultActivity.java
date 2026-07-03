package com.timnieto.profileregistration;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class ResultActivity extends AppCompatActivity {

    private static final String UNKNOWN_VALUE = "Unknown";
    private static final String DATE_INPUT_PATTERN = "yyyy-MM-dd";
    private static final String DATE_OUTPUT_PATTERN = "MMMM d, yyyy";

    private static final Set<String> GLOBE_PREFIXES = Set.of(
            "0817", "0905", "0906", "0915", "0916", "0917", "0926", "0927", "0935",
            "0936", "0937", "0945", "0955", "0956", "0965", "0966", "0967",
            "0975", "0976", "0977", "0995", "0997"
    );

    private static final Set<String> SMART_PREFIXES = Set.of(
            "0811", "0813", "0907", "0908", "0909", "0910", "0912", "0913", "0914",
            "0918", "0919", "0920", "0921", "0928", "0929", "0930", "0938", "0939",
            "0940", "0946", "0947", "0948", "0949", "0950", "0951",
            "0961", "0963", "0968", "0969", "0970",
            "0981", "0989", "0998", "0999"
    );

    private static final Set<String> DITO_PREFIXES = Set.of(
            "0895", "0896", "0897", "0898",
            "0991", "0992", "0993", "0994"
    );

    private static final Set<String> SUN_PREFIXES = Set.of(
            "0922", "0923", "0924", "0925",
            "0931", "0932", "0933", "0934",
            "0941", "0942", "0943", "0944"
    );

    private TextView fullNameTextView;
    private TextView birthdayTextView;
    private TextView ageTextView;
    private TextView zodiacTextView;
    private TextView genderTextView;
    private TextView mobileTextView;
    private TextView telecomTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initializeViews();
        setupClickListeners();
        displayUserProfile();
    }

    private void initializeViews() {
        fullNameTextView = findViewById(R.id.tvFullName);
        birthdayTextView = findViewById(R.id.tvBirthday);
        ageTextView = findViewById(R.id.tvAge);
        zodiacTextView = findViewById(R.id.tvZodiac);
        genderTextView = findViewById(R.id.tvGender);
        mobileTextView = findViewById(R.id.tvMobile);
        telecomTextView = findViewById(R.id.tvTelecom);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
    }

    private void displayUserProfile() {
        String givenName = getIntentValue(RegisterActivity.EXTRA_GIVEN_NAME);
        String surname = getIntentValue(RegisterActivity.EXTRA_SURNAME);
        String birthday = getIntentValue(RegisterActivity.EXTRA_BIRTHDAY);
        String gender = getIntentValue(RegisterActivity.EXTRA_GENDER);
        String mobile = getIntentValue(RegisterActivity.EXTRA_MOBILE);

        fullNameTextView.setText("Full Name: " + givenName + " " + surname);
        birthdayTextView.setText("Birthday: " + formatBirthday(birthday));
        ageTextView.setText("Age: " + calculateAge(birthday));
        zodiacTextView.setText("Zodiac Sign: " + getZodiacSign(birthday));
        genderTextView.setText("Gender: " + gender);
        mobileTextView.setText("Mobile: " + mobile);
        telecomTextView.setText("Telecom: " + getTelecomNetwork(mobile));
    }

    private String getIntentValue(String key) {
        String value = getIntent().getStringExtra(key);
        return value == null ? "" : value;
    }

    private String formatBirthday(String birthday) {
        Date date = parseBirthday(birthday);

        if (date == null) {
            return UNKNOWN_VALUE;
        }

        SimpleDateFormat outputFormat = new SimpleDateFormat(DATE_OUTPUT_PATTERN, Locale.ENGLISH);
        return outputFormat.format(date);
    }

    private String calculateAge(String birthday) {
        Date birthDate = parseBirthday(birthday);

        if (birthDate == null) {
            return UNKNOWN_VALUE;
        }

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return String.valueOf(age);
    }

    private String getZodiacSign(String birthday) {
        Date birthDate = parseBirthday(birthday);

        if (birthDate == null) {
            return UNKNOWN_VALUE;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) return "Aquarius";
        if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) return "Pisces";
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) return "Aries";
        if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) return "Taurus";
        if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) return "Gemini";
        if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) return "Cancer";
        if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) return "Leo";
        if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) return "Virgo";
        if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) return "Libra";
        if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) return "Scorpio";
        if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) return "Sagittarius";

        return "Capricorn";
    }

    private Date parseBirthday(String birthday) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_INPUT_PATTERN, Locale.US);
        inputFormat.setLenient(false);

        try {
            return inputFormat.parse(birthday);
        } catch (ParseException exception) {
            return null;
        }
    }

    private String getTelecomNetwork(String mobile) {
        if (mobile == null || mobile.length() < 4) {
            return "Unknown Network";
        }

        String prefix = mobile.substring(0, 4);

        if (GLOBE_PREFIXES.contains(prefix)) return "Globe / TM";
        if (SMART_PREFIXES.contains(prefix)) return "Smart / TNT";
        if (DITO_PREFIXES.contains(prefix)) return "DITO";
        if (SUN_PREFIXES.contains(prefix)) return "Sun Cellular";

        return "Unknown Network";
    }
}