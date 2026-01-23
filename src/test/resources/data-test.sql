INSERT INTO professor (name, fach_id)
VALUES ('ProfTestName', '11111111-1111-1111-1111-111111111111');

INSERT INTO student (name, fach_id)
VALUES ('StudentTestName', '22222222-2222-2222-2222-222222222222');

INSERT INTO korrektor(name, fach_id)
VALUES ('KorrektorTestName', '33333333-3333-3333-3333-333333333333');

INSERT INTO exam (fach_id, title, professor_fach_id, start_time, end_time, result_time)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'Test 1',
        '11111111-1111-1111-1111-111111111111',
        '2025-06-20 08:00:00',
        '2025-07-02 14:00:00',
        '2025-07-09 14:00:00');

INSERT INTO frage (frage_text, professor_fach_id, exam_fach_id, fach_id, max_punkte, type)
VALUES ('FrageTestText',
        '11111111-1111-1111-1111-111111111111',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        10,
        'SC');

INSERT INTO correct_answers (fach_id, frage_id, richtige_antwort, antwort_optionen)
VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'Loesung1',
        'Loesung1\nLoesung2');

INSERT INTO antwort (fach_id, frage_antwort_id, antwort_text, student_fach_id, antwort_zeitpunkt)
VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'Loesung1',
        '22222222-2222-2222-2222-222222222222',
        '2025-06-20 08:10:00'
        );

INSERT INTO review (fach_id, antwort_fach_id, korrektor_fach_id, bewertung, punkte)
VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        '33333333-3333-3333-3333-333333333333',
        'Bewertung',
        10
        );