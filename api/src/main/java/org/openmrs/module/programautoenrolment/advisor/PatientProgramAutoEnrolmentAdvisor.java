package org.openmrs.module.programautoenrolment.advisor;

import org.aopalliance.aop.Advice;
import org.openmrs.module.programautoenrolment.advice.ANCProgramAutoEnrolment;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniEncounterTransaction;

import java.lang.reflect.Method;

public class PatientProgramAutoEnrolmentAdvisor extends StaticMethodMatcherPointcutAdvisor implements Advisor {
    private static final String SAVE_METHOD = "save";
    private ANCProgramAutoEnrolment ancProgramAutoEnrolment;

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return SAVE_METHOD.equals(method.getName());
    }

    @Override
    public Advice getAdvice() {
        return new AfterReturning();
    }

    private class AfterReturning implements AfterReturningAdvice {
        @Override
        public void afterReturning(Object o, Method method, Object[] transactions, Object o1) throws Throwable {
            if (null == ancProgramAutoEnrolment) {
                ancProgramAutoEnrolment = ANCProgramAutoEnrolment.create();
            }
            ancProgramAutoEnrolment.enrollWithSafety((BahmniEncounterTransaction)transactions[0]);
        }
    }
}
