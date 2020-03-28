# Libraries
library(ggplot2)
library(matrixStats)
ta  <- as.matrix(read.table(file.choose(), sep=",", header = FALSE))
ta <- t(ta)
dim(ta)
head(ta)

# Create dummy data
data <- data.frame(
  y=ta[,1],
  y2=ta[,2],
  y3=ta[,3],
  y4=ta[,4],
  x=seq(1, length(ta))
)
ggplot(data, aes(x)) +
  geom_line(aes(y = y, colour = "var0")) + 
  geom_line(aes(y = y2, colour = "var1")) +
  geom_line(aes(y = y3, colour = "var2")) +
  geom_line(aes(y = y4, colour = "var3")) +
  scale_x_log10( breaks=c(1,5,10,15,20,50,100,200), limits=c(1,200) )

plot(ta, x=x*1000, log="x", type="o")

convergence <- read.csv(file.choose(), header=FALSE, row.names=1)

sds <- rowSds(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
men <- rowMeans(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
print(sds)

# create dummy data
data <- data.frame(
  names=rownames(convergence),
  means=men,
  sds=sds
)

ggplot(data) +
  geom_bar(aes(x=names, y=means), stat="identity", fill="skyblue", alpha=0.7) +
  geom_errorbar( aes(x=names, ymin=means-sds, ymax=means+sds), width=0.4, colour="orange", alpha=0.9, size=1.3) +
  geom_text(aes(label=as.integer(means), x =names, y=means), position=position_dodge(width=0.9), vjust=-0.25) +
  xlab("Epsilon") + ylab("avg. amount of episodes until convergence") 


ba <- barplot(names=rownames(convergence), height=men, ylim=c(0, max(men)*1.2), ylab = "avg. episodes until convergence", xlab = "epsilon value")
text(x = ba, y = men, label = as.integer(men), pos = 3, cex = 0.8, col = "red")
