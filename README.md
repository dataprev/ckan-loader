Documentação do ckan-loader
===========
Conteúdo do repositório:
/env : Exemplo para utilização do ckan-loader
/src : Fontes

Para compilar mudanças no xsd:
> mvn -Pjaxb_generate validate

Para gerar o binário:
> mvn clean package

Após gerar o binário, o mesmo deve ser colocado na pasta env para executar o exemplo.

Para executar a aplicação:
> java -jar ckan-loader.jar env-key

sendo "env-key" a chave definida no arquivo xml de configuração.
