SET NAMES 'utf8';
CREATE DATABASE IF NOT EXISTS orders;
CREATE DATABASE IF NOT EXISTS review;
CREATE DATABASE IF NOT EXISTS product;
CREATE DATABASE IF NOT EXISTS user;

create table if not exists product.category
(
    id   bigint auto_increment primary key,
    name varchar(255)
);

create table if not exists product.product
(
    id          bigint auto_increment primary key,
    description text   not null,
    link_photo  varchar(255),
    name        varchar(255),
    price       double not null,
    category_id bigint not null references category (id)
);

create table if not exists product.product_rating
(
    id           bigint auto_increment primary key,
    product_id   bigint not null references product (id),
    sum_rating   bigint default 0,
    count_rating bigint default 0
);

create table if not exists product.product_popularity
(
    id          bigint auto_increment primary key,
    product_id  bigint not null references product (id),
    count_order bigint default 0
);

CREATE TRIGGER product.insert_product_rating
    AFTER INSERT
    ON product.product
    FOR EACH ROW
    INSERT product.product_rating(product_id) VALUE (New.id);

CREATE TRIGGER product.insert_product_popularity
    AFTER INSERT
    ON product.product
    FOR EACH ROW
    INSERT product.product_popularity(product_id) VALUE (New.id);

insert into product.category
values (1, 'Ноутбуки'),
       (2, 'Планшеты'),
       (3, 'Фототехника'),
       (4, 'Смартфоны'),
       (5, 'Наушники'),
       (6, 'Телевизоры'),
       (7, 'Аксессуары'),
       (8, 'Периферия'),
       (9, 'Мониторы');

insert into product.product(description, link_photo, name, price, category_id)
values ('Ноутбук Tecno T1 i5 16+512G Grey Win11 15.6 — устройство с экраном диагональю 15,6 дюйма и разрешением 1920х1080 пикселей, оснащенное IPS-матрицей, которая формирует контрастное изображение с реалистичной цветопередачей. Покрытие дисплея — матовое, хорошо противостоит бликам при ярком внешнем освещении. Частота обновления стандартная — 60 Гц.
        Для обработки графики и ее дальнейшего вывода на экран используется встроенная видеокарта Intel Iris Xe Graphics. Устройство поставляется вместе с предустановленной ОС Windows 11 «Домашняя», которая предоставляет широкие возможности для персонализации. Корпус выполнен из алюминиевого сплава. Для автономной работы используется встроенная аккумулятор емкостью 6060 мА*ч.
        За высокую производительность отвечает четырехъядерный процессор Intel Core i5 1155G7 с номинальной частотой 2,5 ГГц, которая при высоких нагрузках увеличивается до 4,5 ГГц. Объем кэш-памяти — 8 Мб. Для установки приложений, хранения фото, видео и прочих файлов установлен SSD-накопитель — объем составляет 512 Гб. Предусмотрена оперативная память LPDDR4 на 1 Гб — частота достигает 4266 МГц.',
        '/app/images/1.png', 'Ноутбук TECNO Megabook T1 T15TA Grey', 49999, 1),
       ('Ноутбук Huawei MateBook, серый на операционной системе DOC выполнен в тонком металлическом корпусе габаритами 15,9х322,5х214,8 мм и весом 1,38 кг. Максимальный угол раскрытия крышки девайса — 180°. За вычислительную производительность системы и работу в современных программах отвечают процессор Intel Core i3 1115G4 (два ядра, 3-4,1 ГГц) и графический контроллер UHD Graphics. Размер оперативной DDR4-памяти устройства — 8 Гб, емкость SSD-носителя — 256 Гб.
        IPS-экран диагональю 14 дюймов демонстрирует красочные кадры с точной цветопередачей, разрешением 1920x1080 пикселей и яркостью до 250 нит. Клавиатура с подсветкой, функциональной кнопкой Fn, стандартным touchpad и клавишей включения, совмещенной со сканером отпечатка пальца, обеспечивает безошибочный ввод данных. Веб-камера спрятана под «кнопкой» в верхнем ряду клавиатуры: при нажатии она появляется, повторное нажатие прячет ее обратно. Поддерживаемое разрешение съемки — 1280х720 пикселей. Микрофон обеспечивает двустороннюю голосовую связь во время участия в видеочатах. Акустическая система представлена двумя динамиками с технологией Dolby Atmos.',
        '/app/images/2.png', 'Ноутбук HUAWEI MateBook D 14 i3 1115G4/8/256Gb DOS Space Grayy', 39999, 1),
       ('Игровой ноутбук Thunderobot 911 Air XS D выполнен в классическом пластиковом корпусе со светодиодной подсветкой на крышке и кнопке питания. Габариты модели — 23,9х359х254 мм, вес составляет 2,15 кг. За высокую производительность системы во время игр, стриминга, работы в сложных графических программах отвечают восьмиядерный процессор Intel Core i5 12450H (2-4,4 ГГц) и видеоподсистема GeForce RTX 3050. Размер оперативной памяти составляет 8 Гб, емкость SSD накопителя — 256 Гб. Система охлаждения из двух вентиляторов, охваченных теплотрубками и боковыми радиаторами обеспечивает эффективный отвод тепла.
        Девайс оснащен полуматовым IPS-дисплеем диагональю 15,6 дюйма, который воспроизводит изображения разрешением Full HD с естественной цветопередачей, частотой обновления 144 Гц и яркостью до 300 нит. DLSS технология с использованием нейросетей повышает быстродействие в играх без потери качества картинки.',
        '/app/images/3.png', 'Ноутбук игровой Thunderobot 911 Air XS D', 64999, 1),
       ('Ноутбук Digma DN16R3-8CXW01 разработан для решения учебных, творческих и офисных задач. Девайс функционирует под управлением операционной системы Windows 11 Pro с интуитивно понятным интерфейсом и опциями безопасности. Двухъядерный процессор AMD Ryzen 3 3250U (2,1-3,5 ГГц) и видеокарта RX Vega 3 поддерживают высокую производительность и вычислительную способность системы. Размер оперативной памяти DDR4 So-Dimm ноутбука составляет 8 Гб (с возможностью увеличения до 32 Гб), емкость SSD-накопителя — 256 Гб. Встроенные адаптеры Wi-Fi 802.11 a/b/g/n/ac и Bluetooth версии 5.0 поддерживают беспроводную связь с совместимыми устройствами.',
        '/app/images/4.png', 'Ноутбук Digma DN16R3-8CXW01', 35999, 1),
       ('Ноутбук Apple MacBook Air 13 M1 в алюминиевом корпусе цвета Space Gray оснащен восьмиядерным процессором Apple M1 и семиядерным графическим контроллером. Объем оперативной памяти — 8 Гб, твердотельного накопителя — 256 Гб.
        Экран типа диагональю 13,3 дюйма обладает разрешением 2560х1600 пикселей. Матрица IPS обеспечивает широкие углы обзора. Звуковая система представлена двумя динамиками типа Dolby Atmos. Для беспроводной передачи данных есть модули Bluetooth версии 5.0 и Wi-Fi.
        Клавиатура снабжена подсветкой. В корпус встроены разъем для наушников 3,5 мм и два порта Thunderbolt 3. Эта модель способна работать от аккумулятора в течение 18 часов. В комплект входит блок питания. Размеры лэптопа — 1,61х30,4х21,2 см, вес — 1,29 кг.',
        '/app/images/5.png', 'Ноутбук Apple MacBook Air 13 M1/8/256 Space Gray (MGN63)', 95999, 1),

       ('Планшет Honor Pad X8 LTE Blue (AGM3-AL09DHN) — модель в корпусе из металла, оснащенная аккумулятором емкостью 5100 мА*ч, который обеспечивает работу в автономном режиме без подзарядки до 10 часов. Работа осуществляется на базе платформы Android 12. Устройство оснащено 10,1-дюймовым сенсорным экраном типа TFT IPS разрешением 1920х1200 пикселей, что дает возможность получать картинку с высокой детализацией.
За быстродействие отвечают оперативная память на 4 Гб и восьмиядерный процессор MediaTek Cortex A78/ Cortex A55 с тактовой частотой 1,5 ГГц, увеличивающейся до 2 ГГц. В конструкции предусмотрены основная и фронтальная камера по 5 Мп, которые позволяют получать качественные снимки и видеоролики.',
        '/app/images/6.png', 'Планшет HONOR Pad X8 4/64GB LTE Blue (AGM3-AL09DHN)', 13999, 2),
       ('Планшет Honor Pad X9 Wi-Fi Space Gray 5301AGJC функционирует на базе предустановленной ОС MagicOS 7.1. Модель в алюминиевом корпусе оснащена дисплеем диагональю 11,5-дюймов и разрешением 2000х1200 пикселей, на который выводится сочная, яркая картинка. За счет матрицы IPS обеспечивается получение сочных, насыщенных оттенков. Частота обновления 120 Гц гарантирует плавный вывод изображений, исключает появление задержек в динамичных сценах. Сенсорный экран отличается высокой чувствительностью.',
        '/app/images/7.png', 'Планшет HONOR Pad X9 4/128GB Wi-Fi Space Gray 5301AGJC', 18999, 2),
       ('Планшет Xiaomi Redmi Pad SE 4/128GB Gray (49283) оснащен FHD+ дисплеем диагональю 11". Изображение на экране будет высококачественным и детализированным. Благодаря яркости дисплея 400 нит планшетом удобно пользоваться как в помещении, так и на улице. Частота обновления экрана составляет 90 Гц. Система дополнена обновленным режимом чтения. Для предотвращения утомления глаз предусмотрено сокращенное излучение синего света. За качество звука отвечает технология Dolby Atmos, Hi-Res Audio и четыре встроенных динамика. Цельный корпус планшета выполнен из прочного алюминиевого сплава.',
        '/app/images/8.png', 'Планшет Xiaomi Redmi Pad SE 4/128GB Gray (49283)', 15999, 2),
       ('Планшет Apple iPad 10.2 выполнен в корпусе цвета «серый космос» и оснащен 64 Гб памяти для хранения данных. Для подключения к интернету используется модуль Wi-Fi. Экран планшета диагональю 10,2 дюйма представлен Retina-матрицей разрешением 2160х1620 пикселей с плотностью 264 ppi и олеофобным покрытием, устойчивым к появлению следов от пальцев
Планшет работает на базе шестиядерного микропроцессора A13 Bionic с частотой 2,66 ГГц. Основная камера модели представлена модулем на 8 Мп с с возможностью автоматической стабилизации, съемки HDR-контента, серийной съемки и с возможностью привязки фотографий к месту съемки.',
        '/app/images/9.png', 'Планшет Apple iPad 10.2 Wi-Fi 64GB Space Grey (MK2K3)', 38499, 2),
       ('Планшет Huawei MatePad SE в металлическом корпусе графитового цвета оснащен оперативной памятью объемом 4 Гб и встроенным хранилищем емкостью 128 Гб. Он поддерживает подключение к интернету через SIM-карту вплоть до стандарта 4G LTE, обладает модулями Wi-Fi и Bluetooth.
Быструю и плавную работу обеспечивает восьмиядерный процессор Qualcomm Snapdragon 680 с максимальной тактовой частотой 2.4 ГГц. Эта модель работает на операционной системе Harmony OS 3.0. Экран диагональю 10,36 дюйма сделан на основе IPS-матрицы — изображение четкое при любом освещении, обладает широкими углами обзора. Разрешение дисплея — 2000х1200 пикселей, в нем реализована технология защиты зрения от воздействия вредного синего цвета.',
        '/app/images/10.png', 'Планшет HUAWEI MatePad SE (AGS5-L09) 4/128Gb LTE Black', 20499, 2),

       ('Системный фотоаппарат Sony A6400 + SEL-P1650 Black (ILCE-6400L/B) оснащен технологией автоматической фокусировки с 425 точками, что позволяет захватывать объекты в движении. Кроме того, предусмотрена технология движения за глазами Eye AR.
Фотоаппарат выполнен в эргономичном легком корпусе с удобной рукояткой и кнопками для управления и изменения настроек камеры. Для просмотра фотографий можно воспользоваться поворотным экраном.
Для съемки используется датчик APS-C разрешением 24,2 Мп. Камера работает на базе процессора Bionz X с поддержкой большого количества режимов, включая съемку Time-Lapse.',
        '/app/images/11.png', 'Фотоаппарат системный Sony A6400 + SEL-P1650 Black (ILCE-6400L/B)', 94999, 3),
       ('Описание отсутствует',
        '/app/images/12.png', 'Фотоаппарат системный Nikon Z5 Kit 24-70 f/4 S', 219990, 3),
       ('Системный фотоаппарат Sony Alpha ILCE-6400 Kit ILCE6400YS.CEC Silver с электронным видеоискателем на 2360000 Пикс и 100% площадью покрытия кадра оснащен матрицей размером 23,5x15,6 мм с разрешением 24,2 МПикс и диапазоном ISO 100-25600. Модель с максимальным показателем 6000х4000 Пикс обеспечивает качество видеосъемки 3840x2160 Пикс со скоростью 120 кадр/сек. Доступны алгоритмы сжатия H.264 и MPEG4.',
        '/app/images/13.png', 'Фотоаппарат системный Sony Alpha ILCE-6400 Kit ILCE6400YS.CEC Silver', 145990, 3),
       ('Экшн-камера GoPro HERO9 Black Edition – ваш верный спутник в любых приключениях! Она ведёт съёмку видео в разрешении до 5K Ultra HD (5120 х 2160 пикселей) с частотой 30 к/с, что позволит получить очень чёткие кадры с отличной детализацией. Поддерживается и запись в 4K, 2.7K, 1440p и 1080p.',
        '/app/images/14.png', 'Видеокамера экшн GoPro HERO9 Black Edition (CHDHX-901-RW)', 29999, 3),
       ('Экшн-камера Digma DiCam 450 весит всего 83 г. Она сохраняет истории о ваших приключениях в видеоформате с разрешением до 4K.
СОВРЕМЕННЫЕ ТЕХНОЛОГИИ
Управляйте съёмкой со смартфона, превратив его в пульт ДУ. Передавайте файлы по Wi-Fi или с помощью проводного подключения.
ПРОСМАТРИВАЙТЕ ГОТОВЫЕ МАТЕРИАЛЫ
Встроенный 2-дюймовый дисплей позволяет сразу проверить качество того, что записано. Также можно прослушивать звук – для этого предусмотрен динамик.',
        '/app/images/15.png', 'Видеокамера экшн Digma DiCam 450 Black', 3640, 3),


       ('Смартфон Apple iPhone 15 Pro Natural Titanium получил дисплей диагональю 6,1 дюйма, выполненный по технологии Super Retina XDR. Разрешение — 2556x1179 пикселей, яркость достигает 2000 кд/м², контрастность составляет 2000000:1 — изображение четкое, детализированное и насыщенное. Частота обновления 120 Гц обеспечивает плавность отображения любого динамичного контента. Экран защищает прочное стекло Ceramic Shield.',
        '/app/images/16.png', 'Смартфон Apple iPhone 15 Pro 128GB Natural Titanium', 149999, 4),
       ('Смартфон Xiaomi Redmi Note 12 Blue оснащен AMOLED-экраном диагональю 6,67 дюйма разрешением 2400x1080 пикселей. Яркость достигает 1200 нит, контрастность — 4500000:1, что обеспечивает четкую, детализированную, насыщенную картинку. Благодаря поддержке технологии Sunlight Display даже под солнцем изображение будет легко различимо. Частота обновления 120 Гц гарантирует плавность воспроизведения динамичных моментов в играх и любого другого контента.',
        '/app/images/17.png', 'Смартфон Xiaomi Redmi Note 12 4/128GB Blue', 13899, 4),
       ('Смартфон Huawei nova 11i Starry Black (MAO-LX9N) представлен в пластиковом корпусе черного цвета. В модель установлена операционная система Android. Предусмотрены два слота под nano-SIM. Телефон функционирует на базе восьмиядерного процессора Qualcomm Snapdragon 680 2,4 ГГц. Оперативная память обладает емкостью 8 Гб, встроенная — объемом 128 Гб.
Сенсорный дисплей смартфона диагональю 6,8 дюйма произведен на основе технологии IPS. Изображение передается в разрешении 2388x1080 пикселей. Частота обновления 90 гц обеспечивает плавную смену кадров без разрывов.',
        '/app/images/18.png', 'Смартфон HUAWEI nova 11i 8/128GB Starry Black (MAO-LX9N)', 16999, 4),
       ('Смартфон Samsung Galaxy A34 Awesome Silver обладает большим экраном диагональю 6,6 дюйма разрешением 2340х1080 пикселей, на котором комфортно играть, смотреть фильмы и видеоролики, общаться в социальных сетях, решать рабочие задачи. Технология Super AMOLED обеспечивает высокую яркость (до 1000 нит) и контрастность, даже при ярком солнце картинка будет различима. Частота обновления 120 Гц способствует плавности отображения любого динамичного контента. Экран покрывает прочное стекло Corning Gorilla Glass 5.
Аппаратная часть представлена восьмиядерным смартфоном Mediatek Dimensity 1080 частотой 2,6 ГГц и 8 Гб оперативной памяти. Для хранения информации предусмотрены 256 Гб встроенной памяти, которую можно расширить, установив карту microSD (до 1024 Гб). Возможна работа с двумя SIM-картами формата nano.',
        '/app/images/19.png', 'Смартфон Samsung Galaxy A34 256GB Awesome Silver', 30499, 4),
       ('iPhone 13. Сильный мира всего.
iPhone 13. Самая совершенная система двух камер на iPhone. Режим «Киноэффект» делает из видео настоящее кино. Супербыстрый чип A15 Bionic. Неутомимый аккумулятор. Прочный корпус. И ещё более яркий дисплей Super Retina XDR.',
        '/app/images/20.png', 'Смартфон Apple iPhone 13 128GB nanoSim/eSim Midnight', 79999, 4),

       ('Наушники Apple AirPods 3rd generation (MPNY3) — модель в корпусе белого цвета с эргономичными вкладышами, форма которых обеспечивает комфортную посадку. Стандарт пыле- и влагозащиты — IPX4. Устройство обладает эквалайзером, который позволяет менять настройки воспроизведения. Встроенный датчик кожи гарантирует быстрое включение и выключение. Синхронизация со смартфоном и планшетом осуществляется посредством Bluetooth версии 5.0. Модель оснащена кнопкой для регулирования режима работы, уровня громкости и активации голосового помощника.',
        '/app/images/21.png', 'Наушники Apple AirPods 3rd generation (MPNY3)', 18999, 5),
       ('Наушники Apple AirPods Pro (MTJV3) — модель типа вкладыши в корпус из пластика, амбушюры сделаны из силикона. Благодаря стандарту IPX4 модель не боится попадания воды. Девайс поддерживает опцию беспроводного соединения с источником звука за счет наличия встроенного Bluetooth-адаптера версии 5.3. Радиус действия работы достигает 10 м, что дает возможность свободно перемещаться по квартире без смартфона в руке. Управление параметрами производится посредством сенсорной панели.',
        '/app/images/22.png', 'Наушники Apple AirPods Pro (2nd gen) MagSafe Case USB-C (MTJV3)', 29999, 5),
       ('Наушники True Wireless Huawei Freebuds 5i Ceramic White (T0014) поставляются с тремя парами силиконовых насадок разного размера. Амбушюры позволяют выбрать наиболее комфортный вариант и обеспечивают максимальное прилегание к ушной раковине. Динамики диаметром 10 мм отвечают за воспроизведение объемного звука.',
        '/app/images/23.png', 'Наушники True Wireless HUAWEI Freebuds 5i Ceramic White (T0014)', 5999, 5),
       ('Внутриканальные наушники Apple EarPods with Lightning Connector подключаются к источнику звука по кабелю с портом Lighting. Модель снабжена динамиками с частотным диапазоном от 20 Гц до 20 кГц и панелью управления с микрофоном на кабеле, которая позволяет менять громкость композиций и переключать их долгим нажатием на кнопку. Длина кабеля — 1,2 м.',
        '/app/images/24.png', 'Наушники внутриканальные Apple EarPods with Lightning Connector (MMTN2)', 2699, 5),
       ('Накладные Bluetooth-наушники Soundcore Life Q30 (A3028) черного цвета поддерживают беспроводное подключение и гибридную систему шумоподавления. В каждой чаше находится по одному внутреннему и по одному внешнему микрофону. Пластиковый корпус дополнен амбушюрами из искусственной кожи с эффектом памяти и поворотными чашами. Встроенная батарея на 720 мА*ч обеспечивает до 40 часов работы в режиме ожидания. В комплект входит чехол и кабель для зарядки. Чувствительность микрофона составляет 93 дБ.',
        '/app/images/25.png', 'Наушники полноразмерные Bluetooth Soundcore Life Q30 Black (A3028)', 9999, 5),

       ('Телевизор Toshiba 40V35LE с операционной системой Android 11 с интуитивно понятным интерфейсом. Благодаря безрамочному экрану просматривать фильмы и сериалы можно в комфортных условиях. Экран с технологией IPS и разрешением Full HD 1920x1080 Пикс обеспечивает оптимальное соотношение яркости и контрастности.',
        '/app/images/26.png', 'Телевизор Toshiba 40V35LE', 24999, 6),
       ('Hisense 65A6BG — телевизор с экраном формата 16:9, разрешение которого составляет 3840 х 2160 пикселей. Он поддерживает решения HDR10 Dolby Vision и благодаря этому реалистично воспроизводит основные цвета и тончайшие оттенки, а детали в наиболее светлых и самых темных участках изображения хорошо различимы. Искусственный интеллект, распознающий 11 сценариев, автоматически оптимизирует картинку.',
        '/app/images/27.png', 'Телевизор Hisense 65A6BG', 44999, 6),
       ('Телевизор Samsung UE65CU8500U основан на процессоре Crystal 4K и ОС Tizen. Модель получила 65-дюймовую панель с частотой обновления 60 Гц. Прибор поддерживает технологию HDR, цифровое шумоподавление и Smart TV. За чистое звучание отвечает акустика Dolby Digital Plus мощностью 20 Вт. Благодаря встроенным модулям Bluetooth и Wi-Fi можно управлять устройством при помощи смартфона.',
        '/app/images/28.png', 'Телевизор Samsung UE65CU8500U (2023)', 89999, 6),
       ('Телевизор Samsung QE85QN90BAU — модель с установленной ОС Tizen OS, характеризующейся простым и интуитивно понятным интерфейсом. Устройство оснащено 85-дюймовым (214 см) экраном формата 16:9, произведенным с использованием QLED-технологии, которая отвечает за высокую яркость и контрастность. Разрешение 3840x2160 пикселей гарантирует детализацию, яркость и четкость картинки. Частота обновления равна 144 Гц, что исключает вероятность появления разрывов в кадре. Предусмотрена система обработки изображения — масштабирование до 4K Ultra HD.',
        '/app/images/29.png', 'Телевизор Samsung QE85QN90BAU', 449999, 6),
       ('Toshiba 55C450KE — телевизор с 55-дюймовым QLED-экраном, разрешение которого — 3840 х 2160 пикселей. Поддерживается технология HDR10+. Для улучшения изображения есть масштабирование до формата 4K и цифровое шумоподавление.',
        '/app/images/30.png', 'QLED Телевизор Toshiba 55C450KE', 49999, 6),

       ('Смарт-часы Xiaomi Redmi Watch 3 ActiveM2235W1 — модель в корпусе черного цвета, обладающая квадратным сенсорным дисплеем типа AMOLED диагональю 1,83 дюйма (4,65 см), на который выводится картинка разрешением 240х280 пикселей и яркостью 450 кд/м². Металлическая конструкция обеспечивает надежную защиту устройства от повреждений. Скругленное 2,5D-стекло упрощает процесс эксплуатации. При желании можно изменять дизайн циферблата.',
        '/app/images/31.png', 'Смарт-часы Xiaomi Redmi Watch 3 ActiveM2235W1', 4199, 7),
       ('Смарт-часы Apple Watch SE выполнены из алюминия цвета Midnight с ремешком из фторкаучука, длина которого регулируется от 150 мм до 200 мм. Размер экрана — 44 мм. Часы можно погружать в воду на глубину до 50 м. Дисплей представлен матрицей OLED LTPO с технологией Retina, яркостью 100 кд/м² и разрешением 324х394 пикселя.
Часы получают питание от встроенного литий-ионного аккумулятора, емкость которого рассчитана на 18 часов работы. Для зарядки в комплекте есть кабель с креплением на магните.',
        '/app/images/32.png', 'Смарт-часы Apple SE 44mm Midnight Alum / Sport M/L (MNTG3)', 32199, 7),
       ('Смарт-часы Huawei Watch GT4 PNX-B19 Black оборудованы сенсорным AMOLED-экраном высокого разрешения. Устройство совместимо с операционной системой Android. Данная модель поддерживает беспроводные технологии NFC, Bluetooth и Wi-Fi. Функционал устройства включает в себя несколько видов тренировок — бег, велоспорт, плавание, туризм, фитнес. Данная модель фиксирует пульс, уровень кислорода в крови, количество шагов, расход калорий и продолжительность сна.',
        '/app/images/33.png', 'Смарт-часы HUAWEI Watch GT4 PNX-B19 Black', 19999, 7),
       ('Смарт-часы Samsung Galaxy Watch6 40 мм White Gold (SM-R930) обладают дисплеем диагональю 1,47 дюйма и разрешением 480x480 пикселей — увеличить экран и добавить пространства для свайпов позволила тонкая рамка. Надежную защиту от случайных ударов обеспечивает прочное сапфировое стекло.
Объем оперативной памяти — 2 Гб, встроенной — 16 Гб. Гаджет работает как фитнес-трекер и предлагает несколько десятков различных тренировочных режимов, причем некоторые (бег, езда на велосипеде, ходьба) определяются автоматически. Кроме того, устройство следит за состоянием организма: отслеживает пульс и давление, определяет уровень кислорода в крови и температуру кожи, поддерживает функцию ЭКГ, монитор сон и анализирует его, предупреждает при обнаружении нерегулярного сердечного ритма.',
        '/app/images/34.png', 'Смарт-часы Samsung Galaxy Watch6 40 mm White Gold (SM-R930)', 23499, 7),
       ('Смарт-часы Apple Watch S9 45 мм Midnight Aluminium M/L оснащены сенсорным экраном Retina LTPO OLED, обладающий высокой яркостью 2000 нит, что позволяет легко считывать информацию даже под солнечными лучами. Корпус изготовлен из алюминия и защищен от проникновения влаги — выдерживает погружение под воду на глубину до 50 м.
Новый чип S9 с 5,6 млрд транзисторов обеспечивает стабильную работу, быстрый отклик и поддержку двойного касания: чтобы принять входящий звонок, посмотреть поступившее уведомление, контролировать воспроизведение музыки нужно дважды прикоснуться кончиком указательного пальца к подушечке большого. Это особенно удобно, если руки заняты и нет возможности касаться непосредственно экрана часов.',
        '/app/images/35.png', 'Смарт-часы Apple Watch S9 45mm Midnight Aluminium M/L', 59999, 7),

       ('Logitech G102 LightSync — игровая мышь в черном пластиковом корпусе с настраиваемой RGB-подсветкой, поддерживающей 16 млн цветов. Конфигурация разработана для правой руки. Весит конструкция 85 г. Разрешение регулируется в диапазоне от 200 до 8000 точек на дюйм, что позволяет точно подобрать чувствительность и скорость отклика.',
        '/app/images/36.png', 'Игровая мышь Logitech G102 LightSync Black', 2299, 8),
       ('Игровая мышь Logitech G502 (910-005470) — проводная модель. Подключение к совместимым устройствам осуществляют с помощью USB-кабеля. Длина составляет 2,1 м. Девайс с подсветкой отличается эргономичной формой. 11 кнопок обладают возможностью настройки. Управление осуществляется правой рукой. Ножки сделаны из тефлона и предотвращают скольжение по поверхности стола или коврика. Максимальное разрешение сенсора — 25600 dpi. Корпус изготовлен из черного пластика. Размеры — 4х7,5х13,2 см. Вес — 0,121 кг.',
        '/app/images/37.png', 'Игровая мышь Logitech G502 HERO', 4299, 8),
       ('Беспроводная мышь Huawei CD23 55035373 выполнена в черном корпусе с прорезиненным покрытием. Дизайн разработан для работы любой рукой. Устройство сопрягается с компьютером на расстоянии до 10 метров при помощи интерфейса Bluetooth. Оптическое разрешение мыши — 1200 т/д. Питание устройства происходит за счет одной батарейки типа АА в комплекте. В комплекте USB-кабель для зарядки.',
        '/app/images/38.png', 'Мышь беспроводная HUAWEI CD23 55035373', 2999, 8),
       ('Клавиатура Red Square Keyrox TKL g3ms Purple игрового типа оборудована переключателями, внутри которых расположена смазанная пружина. Открытый корпус сделан из качественной пластмассы. Есть световая индикация включения. Для подключения к стационарному компьютеру или ноутбуку используется вход USB 2.0. Кабель несъемного типа покрыт тканевой оплеткой, длина составляет 1,8 м.
Время отклика не превышает 1 мсек. Встроено 87 клавиш с яркой RGB-подсветкой. Есть функция ручной блокировки клавиатуры. Сила нажатия равна 50 г. Под пробелом установлены вставки из резины. Стабилизаторы кнопок смазаны вручную. Вес аксессуара — 880 г.',
        '/app/images/39.png', 'Игровая клавиатура Red Square Keyrox TKL g3ms Purple (RSQ-20032)', 5999, 8),
       ('Выйди победителем из любого онлайн-сражения с клавиатурой Razer Cynosa Lite! Специально разработанные мембранные клавиши обеспечивают высокую производительность в игре, а ты будешь уверен в точности нажатия и попадания в цель. Эта модель оснащена RGB-подсветкой, поддерживающей различные цветовые сценарии из 16,8 миллионов оттенков. Настраивай геймерское место по своему настроению и желанию. В приложении Razer Synape 3 можно изменить назначение всех клавиш и по ходу каждой игры записывать необходимые макросы.',
        '/app/images/40.png', 'Игровая клавиатура Razer Cynosa Lite (RZ03-02741500-R3R1)', 2799, 8),

       ('Huawei MateView GT 27'' XWU-CBA — игровой монитор с изогнутым экраном. Он создан на базе матрицы VA. Разрешение дисплея — 2560 х 1440 пикселей, частота обновления — 165 Гц, время отклика — 4 мсек.
Максимальные углы обзора по вертикали и горизонтали — 178°. Есть антибликовое покрытие. В комплекте идет настольная подставка. Для подключения к системному блоку предусмотрены входы HDMI и DisplayPort. Размеры модели — 61 х 54 х 22 см, вес — 7, 8 кг.',
        '/app/images/41.png', 'Монитор игровой HUAWEI MateView GT 27" VA черный XWU-CBA', 24999, 9),
       ('Игровой монитор Acer Nitro QG270S3bipx оснащен экраном диагональю 27 дюймов с соотношением сторон 16:9, на который выводится картинка разрешением 1920х1080 пикселей. Тонкие рамки позволяют полнее погрузиться в происходящее. Используемый тип матрицы — VA. Широкие углы обзора (по 178° по вертикали и горизонтали) способствуют выдаче четкой картинки с любого ракурса. Частота обновления кадров 180 Гц исключает появление задержек, за счет чего можно мгновенно реагировать на изменения на дисплее яркостью 250 кд/м² и контрастностью 3000:1.',
        '/app/images/42.png', 'Монитор игровой Acer Nitro 27" VA черный QG270S3bipx', 15999, 9),
       ('Утончённый дизайн Acer ED270UPbiipx моментально приковывает к себе взгляды – он с лёгкостью впишется в интерьер и станет отличным дополнением для мощной игровой системы, позволяя насладиться качественной картинкой. Пора забыть про Full HD – девайс выводит на 27-дюймовый экран изображение в разрешении QHD, что обеспечивает высокую детализацию графики. Попробуй оценить виртуальные красоты, выкрутив настройки на максимум. Кроме того, монитор обладает изогнутым дисплеем с радиусом кривизны 1500R, чтобы ты спокойно следил за происходящим периферическим зрением.',
        '/app/images/43.png', 'Монитор игровой Acer 27" VA черный ED270UPbiipx', 18999, 9),
       ('Оригинальный монитор Rombica SkyView M23-MF разработан на базе использования инновационных технологий, интуитивно понятного и простого в обучении интерфейса. Где вы применяете компьютер? В офисе для разработки макетов, корректировки проектов, ввода данных или дома для погружения в волшебный игровой мир? Это устройство обеспечивает оптимальный уровень комфорта независимо от целей и задач.',
        '/app/images/44.png', 'Монитор Rombica SkyView 23" TN черный M23-MF MUT-002', 5999, 9),
       ('Игровой монитор Samsung Odyssey G3 (LS27AG320NIXCI) обладает диагональю 27 дюймов и разрешением 1920х1080 пикселей. Формат экрана — 16:9. Время отклика минимальное — не превышает 1 мс. Уровень яркости достигает 250 кд/м², показатель контрастности — 3000:1. Устройство характеризуется широкими углами обзора, которые достигают 178° как по горизонтали, так и по вертикали. В качестве матрицы используется VA. Набор интерфейсов включает DisplayPort, HDMI и вход 3,5 мм. Поддержка технологии AMD FreeSync Premium обеспечивает плавность изображения — технология снижает замирание картинки и ее дрожание.',
        '/app/images/45.png', 'Монитор игровой Samsung Odyssey G3 27" VA черный LS27AG320NIXCI', 20999, 9);

create table if not exists user.users
(
    id         bigint auto_increment primary key,
    email      varchar(255) null,
    is_enabled bit          not null,
    last_name  varchar(255) null,
    link_photo varchar(255) null,
    name       varchar(255) null,
    password   varchar(255) null
);

insert into user.users(email, is_enabled, last_name, link_photo, name, password)
values ('ivanova.a@yandex.ru', true, 'Иванова', '/app/images/1.0.jpg', 'Анастасия',
        '$2a$10$t0q6oW9/t0/8rEvpeeUiq.W/8JdrOSsjbXNZNEGV.yy4jsOx81ToG'), #123456 password
       ('a.petrov@yandex.ru', true, 'Петров', '/app/images/2.0.jpg', 'Андрей',
        '$2a$10$t0q6oW9/t0/8rEvpeeUiq.W/8JdrOSsjbXNZNEGV.yy4jsOx81ToG'), #123456 password
       ('danil@yandex.ru', true, 'Цыганов', '/app/images/3.0.jpg', 'Данил',
        '$2a$10$t0q6oW9/t0/8rEvpeeUiq.W/8JdrOSsjbXNZNEGV.yy4jsOx81ToG'), #123456 password
       ('vlad123@yandex.ru', true, 'Мартынов', '/app/images/4.0.jpg', 'Владислав',
        '$2a$10$t0q6oW9/t0/8rEvpeeUiq.W/8JdrOSsjbXNZNEGV.yy4jsOx81ToG'), #123456 password
       ('ivanova.a2@yandex.ru', true, 'Иванова', '/app/images/5.0.jpg', 'Алена',
        '$2a$10$t0q6oW9/t0/8rEvpeeUiq.W/8JdrOSsjbXNZNEGV.yy4jsOx81ToG'); #123456 password

insert into user.user_jwt(token, user_id)
values ('eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJiZjc0YTBiMjkyOGI0YjA0ODAwODc4YTZjNmYwYTI1YyIsInN1YiI6InZsYWQxMjNAeWFuZGV4LnJ1IiwiaWF0IjoxNzEwMDE2MDEzLCJleHAiOjE3NDE1NTIwMTJ9.PBJQbK_5Zi3EKwSCp0Zt15WxdMEVG7Z0fBTSH6KLHi-E1ir7NHjmE_GyAimNQQUBpKYG4no-mrMq4NF8lbhA1Q',
        4),
       ('eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI0NWQwN2E2YzYzZmU0Y2EwYjgxZmU1NzhkNTQ1ZWJkYiIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MDgsImV4cCI6MTc0MTU1NDUwOH0.jevXRK5k0sFz1Dcalj_tigqsusLvMkmII4JpG9_zLEPdZZZYPECBtdTHBoXWdIqcIk_ASWGEynl_I9chuDA5WA',
        1);

insert into user.user_refresh_token(token, user_id)
values ('eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxYzQzNmY0NjVmNTM0MWM5YjIzYjI4ZDI5MzQzZGRmZSIsInN1YiI6InZsYWQxMjNAeWFuZGV4LnJ1IiwiaWF0IjoxNzEwMDE2MDQwLCJleHAiOjE3NDE1NTIwNDB9.7zNV7_i7tjID_NuTCT0uMNLhVWcB56VYKunwQzggrknGKcAQinEmA2r8Y_RzTGojh9Q4gEZc-YJOGp_oA9LhDg',
        4),
       ('eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJjZDc0MThiNjRkMjU0NWU4YWI2ZjZhYWFjMjQ5NTEyNCIsInN1YiI6Iml2YW5vdmEuYUB5YW5kZXgucnUiLCJpYXQiOjE3MTAwMTg1MjYsImV4cCI6MTc0MTU1NDUyNn0.XN6IuZ9pbhJz6tCvNxUqxl1FlKQw4DK6ByBEFY6Aeh95Yj6qvwC8lDhXHnxfH6Czx3BzZRrowCER3hhDBZqyyQ',
        1);

create table if not exists review.review
(
    id         bigint auto_increment primary key,
    date       bigint not null,
    product_id bigint not null,
    rate       int    not null,
    text       text   null,
    user_id    bigint not null
);

CREATE PROCEDURE review.update_product_rating(IN product_id_in BIGINT, IN count_rate int)
BEGIN
    UPDATE product.product_rating
    SET count_rating = count_rating + count_rate,
        sum_rating   = (SELECT sum(review.rate)
                        from review
                        where review.product_id = product_id_in)
    WHERE product.product_rating.product_id = product_id_in;
END;

CREATE TRIGGER review.set_product_rating_by_review_insert
    AFTER INSERT
    ON review.review
    FOR EACH ROW
    call review.update_product_rating(NEW.product_id, 1);

CREATE TRIGGER review.set_product_rating_by_review_update
    AFTER UPDATE
    ON review.review
    FOR EACH ROW
    call review.update_product_rating(NEW.product_id, 0);

ALTER TABLE review.review
    ADD CONSTRAINT constraintname UNIQUE (user_id, product_id);
insert into review.review (date, product_id, rate, text, user_id)
values (1701615797610, 1, 10,
        'Легкий и мощный компаньон: этот ноутбук стал для меня настоящим помощником в повседневных задачах благодаря своей портативности и высокой производительности.',
        1),
       (1701615797610, 2, 9,
        'Элегантный дизайн и высококачественный дисплей делают использование этого ноутбука удовольствием. Батарея также впечатляет своей долговечностью.',
        5),
       (1701615797610, 3, 7,
        'Сверхтонкий корпус и отличная автономность - идеальный выбор для тех, кто ценит стиль и мобильность. Очень доволен своим выбором.',
        3),
       (1701615797610, 4, 8,
        'Невероятная производительность в таком компактном форм-факторе. Запуск приложений и многозадачность идут на ура, а качество сборки на высоте.',
        3),
       (1701615797610, 5, 10,
        'Отличное соотношение цена-качество. Ноутбук справляется со всеми моими требованиями без излишеств, и это радует.',
        5),
       (1701615797610, 1, 9,
        'Клавиатура с подсветкой и четкий дисплей делают работу в темное время суток удобной. Дизайн ноутбука также приятно выделяется.',
        3),
       (1701615797610, 2, 9,
        'Этот ноутбук идеально подходит для игр благодаря мощной графике. Плавный геймплей и высокое разрешение делают игровые вечера за ним незабываемыми.',
        2),
       (1701615797610, 3, 4,
        'Ноутбук оказался настоящим разочарованием: постоянные зависания и медленная работа делают его использование крайне неудобным.',
        1),
       (1701615797610, 4, 2,
        'Очень греется при длительной работе, что делает его использование на коленях почти невозможным. Качество сборки также оставляет желать лучшего.',
        4),
       (1701615797610, 3, 2,
        'Клавиатура начала залипать через короткое время использования, что создает постоянные проблемы при наборе текста. Ремонт не помог – качество материалов под вопросом.',
        4),
       (1701615797610, 3, 8,
        'Этот товар стал незаменимым помощником в повседневной жизни. Я не могу представить, как я раньше обходился без него.',
        5),
       (1701615797610, 3, 5,
        'Этот товар - двойственное впечатление. С одной стороны, его функциональность и инновационные возможности поражают. Это может быть вопросом для тех, кто ценит эстетику в своих устройствах.',
        2),
       (1701615797610, 7, 10,
        'Этот товар стал настоящим открытием для меня! Я в восторге от его функциональности и стильного дизайна.',
        1),
       (1701615797610, 12, 9,
        'Кажется, что создатели этого товара прочитали мои мысли. Все, что я хотел, оказалось в одном устройстве. Впечатлен!',
        2),
       (1701615797610, 17, 8,
        'Удивительная вещь! Это нечто совершенно новое и необычное. Я не могу нарадоваться своей покупке.',
        3),
       (1701615797610, 22, 9,
        'Очень удобный и легкий в использовании товар. Яркий пример инноваций в сфере технологий. Рекомендую!',
        4),
       (1701615797610, 27, 8,
        'Когда я впервые включил этот товар, я словно попал в будущее. Функционал поражает, и я не перестаю изучать все его возможности.',
        5),
       (1701615797610, 32, 10,
        'Простота в управлении и высокая производительность – именно то, что я искал. Этот товар оправдал мои ожидания.',
        1),
       (1701615797610, 37, 10,
        'Эстетика этого товара заставляет его выделяться среди подобных устройств. Очень красивый и стильный дизайн.',
        2),
       (1701615797610, 42, 9,
        'Батарея этого товара просто поразительна! Заряд держится очень долго, что особенно важно в путешествиях.',
        3),
       (1701615797610, 24, 7,
        'Надежность и прочность – вот основные качества этого товара. Я уверен, что он прослужит мне долгие годы.',
        4),
       (1701615797610, 16, 8,
        'Этот товар стал незаменимым помощником в повседневной жизни. Я не могу представить, как я раньше обходился без него.',
        5);

create table if not exists orders
(
    id         bigint auto_increment primary key,
    created_at datetime     null,
    status     varchar(255) null,
    updated_at datetime     null,
    user_id    bigint       null
);

create table if not exists order_product
(
    id         bigint auto_increment primary key,
    count      int    null,
    product_id bigint null,
    order_id   bigint not null references orders (id)
);

CREATE TRIGGER orders.set_product_popularity
    AFTER UPDATE
    ON orders.orders
    FOR EACH ROW
BEGIN
    IF OLD.status = 'IN_PROGRESS' THEN
        BEGIN
            UPDATE product.product_popularity
            SET count_order = product_popularity.count_order + 1
            WHERE product_id in (SELECT product_id
                                 FROM order_product
                                          JOIN orders.orders o on o.id = order_product.order_id
                                 WHERE o.id = NEW.id);
        END;
    END IF;
END;
