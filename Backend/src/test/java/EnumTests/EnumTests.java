package EnumTests;


import com.csci5308.codeLabeller.Enums.JwtEnum;
import com.csci5308.codeLabeller.Enums.JwtNumbers;
import com.csci5308.codeLabeller.Enums.MiscEnums;
import com.csci5308.codeLabeller.Enums.SurveyApprovalStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnumTests {

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void JwtEnumTest(){
        Assertions.assertTrue(JwtEnum.AUTHORIZATION.toString().equals("AUTHORIZATION"));
        Assertions.assertTrue(JwtEnum.Bearer.toString().equals("Bearer"));
        Assertions.assertTrue(JwtEnum.Authority.toString().equals("Authority"));
    }

    @Test
    public void JwtNumbersTest(){
        Assertions.assertTrue(JwtNumbers.BearerMark.getValue()==7);
        Assertions.assertTrue(JwtNumbers.JwtTokenHours.getValue()==10);
        Assertions.assertTrue(JwtNumbers.Seconds.getValue()==60);
        Assertions.assertTrue(JwtNumbers.Minutes.getValue()==60);
        Assertions.assertTrue(JwtNumbers.Miliseconds.getValue()==1000);
    }

    @Test
    public void MiscEnumsTest(){
        Assertions.assertTrue(MiscEnums.NumberOfPages.getValue()==1);
    }

    @Test
    public void SurveyApprovalStatusTest(){
        Assertions.assertTrue(SurveyApprovalStatus.APPROVAL.toString().equals("APPROVAL"));
        Assertions.assertTrue(SurveyApprovalStatus.PENDING.toString().equals("PENDING"));
    }


}
