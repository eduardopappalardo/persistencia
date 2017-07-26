package persistencia.anotacao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Atributo {
	public String nome() default "";
	//public boolean autoNumeracao() default false;
	public boolean chavePrimaria() default false;
	public boolean chaveEstrangeira() default false;
	public boolean ignoraAtributo() default false;
	public boolean permiteValorNulo() default false;
}