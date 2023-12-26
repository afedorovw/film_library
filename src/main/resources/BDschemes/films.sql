INSERT INTO role (id, description, title)
SELECT 1, 'Роль пользователя', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 1)
UNION ALL
SELECT 2, 'Роль модератора', 'MODERATOR'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 2);

--Режиссеры
drop sequence directors_sequence;
create sequence directors_sequence;
alter sequence directors_sequence owner to user1;

truncate table films_directors;
truncate table directors  cascade;
INSERT INTO directors  (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Гай Ричи', 1,
		'Британский кинорежиссёр, сценарист и продюсер, чаще всего работающий в жанре криминальной комедии', 
		'1968-09-10', 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/GuyRitchiebyKathyHutchins.jpg/250px-GuyRitchiebyKathyHutchins.jpg');
INSERT INTO directors (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Мартин Скорсезе', 0,
        'В кинематографических кругах Мартин известен как мастер гангстерских лент',
        '1942-11-17', 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Martin_Scorsese_Berlinale_2010_%28cropped2%29.jpg/220px-Martin_Scorsese_Berlinale_2010_%28cropped2%29.jpg');
INSERT INTO directors (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Стэнли Кубрик', 0,
        'Один из самых влиятельных кинематографистов второй половины XX века',
        '1928-07-26', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Kubrick_on_the_set_of_Barry_Lyndon_%281975_publicity_photo%29_crop.jpg/274px-Kubrick_on_the_set_of_Barry_Lyndon_%281975_publicity_photo%29_crop.jpg');
INSERT INTO directors (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Оливер Стоун', 0,
        'Обладатель трёх премий «Оскар»',
        '1946-09-15', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Oliver_Stone_by_Gage_Skidmore.jpg/240px-Oliver_Stone_by_Gage_Skidmore.jpg');
INSERT INTO directors (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Квентин Тарантино', 0,
        'Один из наиболее ярких представителей постмодернизма в независимом кинематографе',
        '1963-03-27', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Quentin_Tarantino_by_Gage_Skidmore.jpg/220px-Quentin_Tarantino_by_Gage_Skidmore.jpg');
INSERT INTO directors (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Стивен Спилберг', 0,
		'Один из самых коммерчески успешных режиссёров в истории мирового кинематографа', 
		'1946-12-18', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Ready_Player_One_Japan_Premiere_Red_Carpet_Steven_Spielberg_%2841604920761%29_%28cropped%29.jpg/240px-Ready_Player_One_Japan_Premiere_Red_Carpet_Steven_Spielberg_%2841604920761%29_%28cropped%29.jpg');
INSERT INTO directors (id, created_by, created_when, directors_fio, country, directors_bio, birth_date, photo)
VALUES (nextval('directors_sequence'), 'admin', 'now()', 'Кристофер Нолан', 1,
		'Приобрёл популярность и хорошие отзывы критиков благодаря трилогии о Тёмном Рыцаре', 
		'1970-07-30', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Christopher_Nolan_Cannes_2018.jpg/230px-Christopher_Nolan_Cannes_2018.jpg');

--Фильмы
drop sequence films_sequence;
create sequence films_sequence;
alter sequence films_sequence owner to user1;
truncate table films cascade;
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 4, null, '2017', 'Меч короля Артура',
        'Молодой Артур живёт на задворках Лондиниума вместе со своей бандой. Он и понятия не имел о собственном королевском происхождении, пока однажды не взял в руки меч Экскалибур',
        'https://www.youtube.com/embed/04PO5hF9Rec');
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 0, null, '2006',  'Отступники',
        'Два лучших выпускника полицейской академии оказались по разные стороны баррикады: один из них – агент мафии в рядах правоохранительных органов, другой – «крот», внедрённый в мафию',
        'https://www.youtube.com/embed/4c8-L3dYBB8');
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 3, null, '1987',  'Цельнометаллическая оболочка',
        'Американская база подготовки новобранцев корпуса морской пехоты. Жесточайшая, бесчеловечная система призвана превратить домашних мальчишек в натренированных хладнокровных убийц',
        'https://www.youtube.com/embed/SypSevvhCw4');
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 3, null, '1986',  'Взвод',
        'Они тоже думали, что будут воевать с партизанами-вьетконговцами, но оказалось, что иногда приходится драться со своими',
        'https://www.youtube.com/embed/T2y3JfJB0dU');
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 2, null, '2019',  'Однажды в… Голливуде',
        '1969 год, золотой век Голливуда уже закончился. Известный актёр Рик Далтон и его дублер Клифф Бут пытаются найти свое место в стремительно меняющемся мире киноиндустрии',
        'https://www.youtube.com/embed/zw81ihoukKU');
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 5, null, '2005',  'Мюнхен',
        'Агент МОССАД и его группа выслеживает и методично уничтожает палестинских террористов, повинных в смерти израильских спортсменов в аэропорту Мюнхена после Олимпиады 1972 года',
        'https://www.youtube.com/embed/feIjYUEWVxk');
INSERT INTO films (id, created_by, created_when, genre, online_copy_path, premier_year, title, description, video)
VALUES (nextval('films_sequence'), 'admin', 'now()', 8, null, '2014', 'Интерстеллар',
        'Когда засуха, пыльные бури и вымирание растений приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется в путешествие',
        'https://www.youtube.com/embed/m2vijtILDuk');

--FILMS_DIRECTORS
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (1, 1);
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (2, 3);
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (3, 4);
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (4, 5);
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (5, 6);
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (6, 2);
INSERT INTO public.films_directors  (film_id, director_id)
VALUES (7, 7);
