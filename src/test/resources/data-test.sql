INSERT INTO professor (id, name)
VALUES ('11111111-1111-1111-1111-111111111111', 'ProfTestName');

INSERT INTO student (id, name)
VALUES ('22222222-2222-2222-2222-222222222222', 'StudentTestName');

INSERT INTO reviewer(id, name)
VALUES ('33333333-3333-3333-3333-333333333333', 'ReviewerTestName');

INSERT INTO exam (id, title, professor_id, start_time, end_time, result_time)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'Test 1',
        '11111111-1111-1111-1111-111111111111',
        '2025-06-20 08:00:00',
        '2025-07-02 14:00:00',
        '2025-07-09 14:00:00');

INSERT INTO question (id, text, exam_id, points, type)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'FrageTestText',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        10,
        'SC');

INSERT INTO correct_answers (id, question_id, solution, choices)
VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'Solution1',
        'Solution1\nSolution2');

INSERT INTO answer (id, question_id, answer, student_id, submit_time)
VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'Solution1',
        '22222222-2222-2222-2222-222222222222',
        '2025-06-20 08:10:00'
        );

INSERT INTO review (id, answer_id, reviewer_id, text, points)
VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        '33333333-3333-3333-3333-333333333333',
        'Text',
        10
        );