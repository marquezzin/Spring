package br.com.marquezzin.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
    // copia propriedades não nulas de um objeto source para um objeto target
    public static void copyNonNullProperties(Object source, Object target) {
        // getnull..: Retorna um array de nomes de propriedades que são nulas no objeto
        // source,sendo ignoradas na cópia.
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    // pegar propriedades nulas
    public static String[] getNullPropertyNames(Object source) {
        // BeanWrapper é uma interface do java que permite acessar as propriedades de um
        // obj
        // WrapperImpl é a implementação dessa interface
        final BeanWrapper src = new BeanWrapperImpl(source);

        // Array com as propriedades do objeto
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // Criação de um conjunto vazio de Strings
        // é utilizada para criar uma coleção de elementos do tipo String que não
        // permite duplicatas.
        Set<String> emptyNames = new HashSet<>();

        // iteração for each do tipo:
        // for (TipoElemento elemento : coleçãoOuArray) {
        // bloco de código a ser executado para cada elemento
        // }
        for (PropertyDescriptor pd : pds) {
            // valor da propriedade atual
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        // array de strings (String[]) com um tamanho baseado no número de elementos em
        // emptyNames
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
