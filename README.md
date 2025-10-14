# java-filmorate
Template repository for Filmorate project.

![ER-Диаграмма для построение базы данных](https://github.com/MadnessLalka/java-filmorate/blob/dev-database/ER-diagram.png) 

# Таблицы базы данных
1) User
2) Friendships
3) FriendshipType
4) LikedFilms
5) Films
6) FilmGenres
7) Genres
8) MPA

# Связи между таблицами
Кроме основных таблиц, таких как User, FrienshipType, Film, Genres и MPA<br>
Есть буферные таблицы для реализации связи многие ко многим - Friendships, LikedFilms, FilmGenres<br>
