create function appendlength (@txt varchar) returns varchar 
as
declare @result varchar
set @result = @txt || 'x'
return @result
//
