package net.franzka.kams.authentication.annotation;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.sql.ast.tree.expression.Over;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
class PasswordConstraintsValidatorTest {

    @InjectMocks
    PasswordConstraintsValidator classUnderTest;

    @Mock
    Password password;

    @Mock
    PasswordValidator passwordValidator;

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    void initializeTest() {

        // Arrange
        // Act
        classUnderTest.initialize(password);

        // Assert
        verify(password, times(1)).message();
    }

    @Test
    void isValidTest() {

        // Arrange
        RuleResult ruleResult = new RuleResult();
        ruleResult.setValid(true);
        when(passwordValidator.validate(any())).thenReturn(ruleResult);
        ReflectionTestUtils.setField(classUnderTest, "passwordValidator", passwordValidator);

        // Act
        boolean result = classUnderTest.isValid("aa", constraintValidatorContext);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void isNotValidTest() {

        // Arrange
        RuleResult ruleResult = new RuleResult();
        ruleResult.setValid(false);
        when(passwordValidator.validate(any())).thenReturn(ruleResult);
        ReflectionTestUtils.setField(classUnderTest, "passwordValidator", passwordValidator);

        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext newConstraintValidatorContext = mock(ConstraintValidatorContext.class);

        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(newConstraintValidatorContext);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilder);

        // Act
        boolean result = classUnderTest.isValid("aa", constraintValidatorContext);

        // Assert
        assertThat(result).isFalse();
    }

}
