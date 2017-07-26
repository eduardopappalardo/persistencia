package persistencia.anotacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AnotacaoEntidade {
	public String nome() default "";
	public String nomeEsquema() default "";
	public String nomeBanco() default "";
}