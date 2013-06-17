package com.lhf.fm.fmrt.fmrt10.domain;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : FMRT1010
 * 설    명 : 
Expression class.tableDesc is undefined on line 6, column 15 in _JavaTemplates.ftl.
The problematic instruction:
----------
==> ${class.tableDesc} [on line 6, column 13 in _JavaTemplates.ftl]
 in include "./_JavaTemplates.ftl" [on line 3, column 1 in lhf-domainTemplate.ftl]
----------

Java backtrace for programmers:
----------
freemarker.core.InvalidReferenceException: Expression class.tableDesc is undefined on line 6, column 15 in _JavaTemplates.ftl.
	at freemarker.core.TemplateObject.assertNonNull(TemplateObject.java:124)
	at freemarker.core.Expression.getStringValue(Expression.java:118)
	at freemarker.core.Expression.getStringValue(Expression.java:93)
	at freemarker.core.DollarVariable.accept(DollarVariable.java:76)
	at freemarker.core.Environment.visit(Environment.java:196)
	at freemarker.core.MixedContent.accept(MixedContent.java:92)
	at freemarker.core.Environment.visit(Environment.java:196)
	at freemarker.core.Environment.include(Environment.java:1375)
	at freemarker.core.Include.accept(Include.java:155)
	at freemarker.core.Environment.visit(Environment.java:196)
	at freemarker.core.MixedContent.accept(MixedContent.java:92)
	at freemarker.core.Environment.visit(Environment.java:196)
	at freemarker.core.Environment.process(Environment.java:176)
	at freemarker.template.Template.process(Template.java:232)
	at com.dwe.codegenerator.startup.CodeGenerator.generatorFile(CodeGenerator.java:293)
	at com.dwe.codegenerator.startup.CodeGenerator.start(CodeGenerator.java:150)
	at com.dwe.codegenerator.startup.CodeGenerator.main(CodeGenerator.java:304)
