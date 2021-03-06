package bpv.utils.validationapi.rule;

import bpv.utils.validationapi.data.Manager;
import bpv.utils.validationapi.rule.data.RuleDescriptor;
import bpv.utils.validationapi.rule.data.ValidationRule;
import bpv.utils.validationapi.rule.resolvers.ValidationCodeResolver;
import bpv.utils.validationapi.rule.resolvers.ValidationCodes;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertNull;

public class RulesGeneratorTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void printGeneratedRules(){
        Set<RuleDescriptor> generatedRules = RulesGenerator.generate(Manager.class);
        for(RuleDescriptor ruleDescriptor : generatedRules){
            logger.info("------------------------------------------------");
            logger.info("Path   : " + ruleDescriptor.getPath());
            for(ValidationRule validation : ruleDescriptor.getRules()){
                logger.info("------------------------------------------------");
                logger.info("Code   : " + validation.getCode());
                logger.info("Message: " + validation.getMessage());
                logger.info("Params : " + validation.getParams());
            }
            logger.info("------------------------------------------------\n");
        }
    }

    @Test
    public void testGenerateRules(){

        Set<RuleDescriptor> generatedRules = RulesGenerator.generate(Manager.class);

        Assert.assertEquals("Number of properties with validation ", 20, generatedRules.size());
        assertManagerRules(generatedRules, "");
    }

    private void assertManagerRules(Set<RuleDescriptor> generatedRules, String rootPath){
        String employess = rootPath + "employees[{position}].";
        String leaders = rootPath + "leaderByGroup[{key}].";
        String bonus = rootPath + "bonus";
        String minLeads = rootPath + "minProjectLeads";

        RuleDescriptor bonusRule = assertAndGetRuleDescriptor(generatedRules, bonus, true);
        RuleDescriptor minLeadsRule = assertAndGetRuleDescriptor(generatedRules, minLeads, true);

        assertValidationRule(bonusRule, bonus, Min.class);
        assertValidationRule(minLeadsRule, minLeads, "MIN_PROJECT_LEADS");

        assertPersonRules(generatedRules, employess);
        assertPersonRules(generatedRules, leaders);

    }

    private void assertPersonRules(Set<RuleDescriptor> generatedRules, String rootPath){
        String name = rootPath + "name";
        String age = rootPath + "age";
        String salary = rootPath + "salary";
        String hobby = rootPath + "hobby";
        String projects = rootPath + "projects";

        RuleDescriptor nameRule = assertAndGetRuleDescriptor(generatedRules, name, true);
        RuleDescriptor ageRule = assertAndGetRuleDescriptor(generatedRules, age, true);
        RuleDescriptor salaryRule = assertAndGetRuleDescriptor(generatedRules, salary, true);
        RuleDescriptor hobbyRule = assertAndGetRuleDescriptor(generatedRules, hobby, false);
        RuleDescriptor projectsRule = assertAndGetRuleDescriptor(generatedRules, projects, true);

        assertValidationRule(nameRule, name, NotNull.class);
        assertValidationRule(ageRule, age, Min.class);
        assertValidationRule(ageRule, age, Max.class);
        assertValidationRule(salaryRule, salary, Min.class);
        assertNull(hobbyRule);
        assertValidationRule(projectsRule, projects, Max.class);

        assertDepartmentRules(generatedRules, rootPath + "department.");
        assertProjectRules(generatedRules, rootPath + "projects[{position}].");

    }

    private void assertDepartmentRules(Set<RuleDescriptor> generatedRules, String rootPath){

        String path = rootPath + "name";

        RuleDescriptor nameRule = assertAndGetRuleDescriptor(generatedRules, path, true);

        Assert.assertEquals("Department Rule:" + path + " Count?", 2, nameRule.getRules().size());

        assertValidationRule(nameRule, path, Min.class);
        assertValidationRule(nameRule, path, NotNull.class);
    }

    private void assertProjectRules(Set<RuleDescriptor> generatedRules, String rootPath){
        String name = rootPath + "name";
        RuleDescriptor nameRule = assertAndGetRuleDescriptor(generatedRules, name, true);
        assertValidationRule(nameRule, name, NotNull.class);
    }

    private RuleDescriptor assertAndGetRuleDescriptor(Set<RuleDescriptor> generatedRules, String path, boolean shouldBePresent){
        RuleDescriptor ruleToFind = new RuleDescriptor(path);

        Optional<RuleDescriptor> result = generatedRules.stream().filter(rule -> rule.equals(ruleToFind)).findAny();
        Assert.assertEquals(path + " Rule Present?", shouldBePresent, result.isPresent());

        return result.isPresent() ? result.get() : null;
    }

    private void assertValidationRule(RuleDescriptor ruleDescriptor, String path, Class<?> clazz){
        Assert.assertTrue( path + " " + clazz.getSimpleName() + "?", ruleDescriptor.getRules().contains(new ValidationRule(ValidationCodeResolver.resolve(clazz))));
    }

    private void assertValidationRule(RuleDescriptor ruleDescriptor, String path, String constraintName){
        String code = ValidationCodes.toValue(constraintName);
        Assert.assertTrue( path + " " + code + " ?", ruleDescriptor.getRules().contains(new ValidationRule(code)));
    }
}
