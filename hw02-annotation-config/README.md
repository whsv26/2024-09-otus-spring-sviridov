Приложение по проведению тестирования студентов (с самим тестированием)

## Цель

Конфигурировать Spring-приложения современным способом, как это и делается в современном мире

## Результат

Готовое современное приложение на чистом Spring

## Описание

- В ресурсах хранятся вопросы и различные ответы к ним в виде CSV файла (5 вопросов).
- Вопросы могут быть с выбором из нескольких вариантов или со свободным ответом - на Ваше желание и усмотрение.
- Программа должна спросить у пользователя фамилию и имя, спросить 5 вопросов из CSV-файла и вывести результат тестирования.
- Выполняется на основе предыдущего домашнего задания плюс, собственно, сам функционал тестирования.

## Требования

- Приложении должно быть основано на [заготовке](https://github.com/OtusTeam/Spring/tree/master/templates/hw02-annotation-config) домашней работы.
- В приложении должна присутствовать объектная модель (отдаём предпочтение объектам и классам, а не строчкам и массивам/спискам строчек).
- Все классы в приложении должны решать строго определённую задачу (см. п. 18-19 "Правила оформления кода.pdf", прикреплённые к материалам занятия).
- Переписать конфигурацию в виде Java + Annotation-based конфигурации. Все зависимости должны быть настроены в IoC контейнере.
- Добавить функционал тестирования студента.
- Добавьте файл настроек для приложения тестирования студентов.
- В конфигурационный файл можно поместить путь до CSV-файла, количество правильных ответов для зачёта - на Ваше усмотрение.
- Помним, CSV с вопросами читается именно как ресурс, а не как файл.
- Нужно написать интеграционный тест класса, читающего вопросы и юнит-тест сервиса с моком зависимости. Под интеграционностью тут понимается интеграция с файловой системой. В остальном, это должен быть юнит-тест
- Файл настроек и файл с вопросами, для тестов д.б. свои.
- Scanner, PrintStream и другие стандартные типы в контекст класть не нужно! См. соответствующие слайды с занятия.
- Весь ввод-вывод осуществляется на английском языке.
