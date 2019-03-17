
Сборка на debuggable изменена
Иконка и название приложения изменены
Открыл crashlytics и firebase init провайдеры

Они конечно ничего не возвращают, но я пытался)) - вот запросы

adb shell content query --uri content://com.justanothertry.slovaizslova.crashlyticsinitprovider --projection context
adb shell content query --uri content://com.justanothertry.slovaizslova.firebaseinitprovider

Пытался записать данные но тот же результат. (Приложения с адекватным ЗАКРЫТЫМ контент провайдером за полночи не нашел, плюс apktool не хотело половину приложений собирать обратно)